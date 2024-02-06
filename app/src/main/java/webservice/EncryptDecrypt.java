package webservice;

    import javax.crypto.Cipher;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Muralidharan
 */
public class EncryptDecrypt {

    private String keyString;
    private String ivString;
    private String strCommonKey = "la_75Eh.8_dJ)fn/QQC@fMk6>?SN/m@0";
    private String strCommonIV = "n<it<8SSSmYngJx>";
    private String strPrivateKey = "s5%OcwjKB8^JW!jjr6RN1B7T@U8rKeUT";
    private String strPrivateIV = "oxuzK443u%aXSEU8";


    public String getEncryptedData(String String_to_encrypt) {
        String strEncryptedData = "";
        try{
            // Encrypting for Data iteration 1
            this.setKey(strPrivateKey, strPrivateIV);
            String strData = this.encrypt(String_to_encrypt);

            // Encrypting for Data iteration 2
            this.setKey(strCommonKey, strCommonIV);
            strEncryptedData = encrypt(strData);
        }catch(Exception e){
            System.out.println("Error while encrypting:"+e.getMessage());
        }
        return strEncryptedData;
    }

    public String getDecryptedData(String String_to_Decrypt){
        String strDecryptedData="";
        try{
            this.setKey(strCommonKey, strCommonIV);
            String strDecryptedMData = this.decrypt(String_to_Decrypt);

            this.setKey(strPrivateKey, strPrivateIV);
            strDecryptedData = this.decrypt(strDecryptedMData);
        }catch(Exception e){
            System.out.println("Error while encrypting:"+e.getMessage());
        }
        return strDecryptedData;
    }

    private boolean setKey(String _keyString, String _ivString) {
        if (_keyString.length() != 32) {
            return false;
        }
        if (_ivString.length() != 16) {
            return false;
        }
        keyString = _keyString;
        ivString = _ivString;
        return true;
    }

    private String encrypt(String stplaintxt)
            throws Exception {
        byte cipherText[] = (byte[]) null;
        byte bytePlaintext[] = stplaintxt.getBytes("WINDOWS-1252");
        byte tdesKeyData[] = keyString.getBytes("WINDOWS-1252");
        Cipher c3des = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(ivString.getBytes("WINDOWS-1252"));
        c3des.init(1, myKey, ivspec);
        cipherText = c3des.doFinal(bytePlaintext);
        return getSFBA(cipherText).toUpperCase();
    }

    private String decrypt(String stEncTxt)
            throws Exception {
        byte tdesKeyData[] = keyString.getBytes("WINDOWS-1252");
        Cipher c3des = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(ivString.getBytes("WINDOWS-1252"));
        c3des.init(2, myKey, ivspec);
        byte cipherText[] = c3des.doFinal(hexSTBA(stEncTxt));
        return new String(cipherText);
    }

    private byte[] hexSTBA(String s) {
        int len = s.length();
        byte data[] = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    private final String getSFBA(byte arrByInput[]) {
        StringBuffer stOutput = new StringBuffer();
        for (int i = 0; i < arrByInput.length; i++) {
            int inHash = arrByInput[i];
            if (inHash < 0) {
                inHash += 256;
            }
            int in1 = inHash / 16;
            int in2 = inHash % 16;
            if (in1 > 9) {
                in1 += 87;
            } else {
                in1 += 48;
            }
            if (in2 > 9) {
                in2 += 87;
            } else {
                in2 += 48;
            }
            stOutput.append((char) in1);
            stOutput.append((char) in2);
        }
        return new String(stOutput);
    }

    public static String calculateChecksum(String data) {
        int chksum = 0;
        for (int j = 0; j < data.length(); j++) {
            chksum = chksum + (((int) data.charAt(j)) * (j + 1));
        }
        return "" + chksum; // Type casting as String as per return type
    }

//    public static void main(String[] args) {
//        EncryptDecrypt ed = new EncryptDecrypt();
//        try{
//            String strEncryptedData=ed.getEncryptedData("Firstline is a good company....");
//            System.out.println("Encrypted:"+strEncryptedData);
//            String strD = ed.getDecryptedData(strEncryptedData);
//            System.out.println("Decryped:"+strD);
//        }catch (Exception e){
//            System.out.println("error:"+e.getMessage());
//        }
//    }
}