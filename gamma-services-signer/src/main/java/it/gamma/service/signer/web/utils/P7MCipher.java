package it.gamma.service.signer.web.utils;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.SignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class P7MCipher {

    private final static String SHA256_ALG = "SHA256WithRSA";

    public static byte[] sign(byte[] aDocument, PrivateKey aPrivateKey, X509Certificate cert) throws Exception {

        // Security.addProvider(new BouncyCastleProvider());
        ArrayList<X509Certificate> certsin = new ArrayList<X509Certificate>();
        certsin.add(cert);

        X509Certificate signingCertificate = certsin.get(0);
        SignerInfoGeneratorBuilder genBuild = new SignerInfoGeneratorBuilder(new BcDigestCalculatorProvider());

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner shaSigner = new JcaContentSignerBuilder(SHA256_ALG).build(aPrivateKey);
        SignerInfoGenerator sifGen = genBuild.build(shaSigner, new X509CertificateHolder(signingCertificate.getEncoded()));
        gen.addSignerInfoGenerator(sifGen);
        gen.addCertificates(new JcaCertStore(certsin));

        CMSTypedData msg = new CMSProcessableByteArray(aDocument);
        CMSSignedData sigData = gen.generate(msg, true); // false=detached

        byte[] encoded = sigData.getEncoded();

        ASN1InputStream in = new ASN1InputStream(encoded);
        CMSSignedData sigData2 = new CMSSignedData(new CMSProcessableByteArray(aDocument), in);

        return sigData2.getEncoded();
    }

}
