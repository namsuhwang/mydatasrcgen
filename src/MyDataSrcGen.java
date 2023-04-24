import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
   java MyDataSrcGen.class BANK_ACC.txt

 */
public class MyDataSrcGen {

    static String basePath = "./data/";
    // static String basePath = "../../../data/";
    static String inFilePath = basePath + "in/";
    static String inFileName = "";

    static String outFilePath = basePath + "out/";
    static String outFileName = "";

    static List<ColInfo> colList = new ArrayList<>();
    static String tableEngName = "";
    static String tableEngNameCm = "";   // 카멜 표기
    static String tableHanName = "";

    public static void main(String[] args) {
        inFileName = args[0];

        String rLine = "";
        int lineCnt = 1;
        try{
            FileReader fileReader = new FileReader(inFilePath + inFileName);	//입력스트림
            BufferedReader bufferedReader = new BufferedReader(fileReader);	//입력 스트림으로부터 문자를 읽을때 버퍼링

            while((rLine = bufferedReader.readLine()) != null) {
                if(lineCnt == 1){
                    tableEngName = rLine.split(",")[0];
                    tableEngNameCm = convCamel(tableEngName);
                    tableHanName = rLine.split(",")[1];
                    System.out.println("테이블명(영문) = " + tableEngName);
                    System.out.println("테이블명(영문카멜) = " + tableEngNameCm);
                    System.out.println("테이블명(한글) = " + tableHanName);
                }else{
                    colList.add(new ColInfo(rLine));
                }
                lineCnt++;
                System.out.println(rLine);
            }


            outFileName = "BankAccController1.java";
            BufferedWriter fileController = getBufferedWriter(outFileName);
            for(int cnt = 0; cnt < colList.size(); cnt++){
                System.out.println("컬럼[" + (cnt+1) + "] = " + colList.get(cnt).toString());
                writer(fileController, colList.get(cnt).toString());
            }
            closeFile(fileController);
            bufferedReader.close();


        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    private static BufferedWriter getBufferedWriter(String outFileName) throws IOException {

        File folder = new File(outFilePath + outFileName);
        if(!folder.exists()) {
            folder.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(folder, true));	//true해줘야 덮어쓰지않음

        return writer;
    }

    private static void closeFile(BufferedWriter writer) throws IOException {
        writer.close();//스트림을 닫음
    }

    private static void writer(BufferedWriter writer, String str) throws IOException {
        // System.out.println(str);
        writer.write(str);
        writer.newLine(); //줄바꿈
    }

    private static void fileWriter(String outFileName, String str) throws IOException {

        File folder = new File(outFilePath + outFileName);
        if(!folder.exists()) {
            folder.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(folder, true));	//true해줘야 덮어쓰지않음
        System.out.println(str);
        writer.write(str);
        writer.newLine(); //줄바꿈
        writer.close();//스트림을 닫음
    }

    public static String convCamel(String v){
        String cv = "";
        char prvCh = ' ';
        char[] chAry = v.toCharArray();
        for(char ch : chAry){
            if(prvCh == ' '){
                cv = String.valueOf(ch).toLowerCase();
            }else{
                if(ch != '_') {
                    if (prvCh == '_') {
                        cv = cv + String.valueOf(ch).toUpperCase();
                    } else {
                        cv = cv + String.valueOf(ch).toLowerCase();
                    }
                }else{
                    cv = cv + "";
                }
            }
            prvCh = ch;
        }

        return cv;
    }


}


class ColInfo {
    String eng_nm;
    String eng_nm_cm;
    String han_nm;
    String data_type;
    Integer data_bef_len;
    Integer data_aft_len;
    Boolean pk_yn;

    public ColInfo() {
    }

    public ColInfo(String lineStr){
        // System.out.println("ColInfo=" + lineStr);
        String[] ary = lineStr.split(",");
        this.eng_nm = ary[0];
        this.eng_nm_cm = MyDataSrcGen.convCamel(ary[0]);
        this.han_nm = ary[1];
        switch (ary[2].toUpperCase()){
            case "STRING":
                this.data_type = "String";
                this.data_bef_len = Integer.valueOf(ary[3].split("/")[0]);
                this.data_aft_len = 0;
                break;
            case "INTEGER":
                this.data_type = "Integer";
                this.data_bef_len = Integer.valueOf(ary[3].split("/")[0]);
                this.data_aft_len = 0;
                break;
            case "FLOAT":
                this.data_type = "Float";
                this.data_bef_len = Integer.valueOf(ary[3].split("/")[0]);
                this.data_aft_len = Integer.valueOf(ary[3].split("/")[1]);
                break;
            case "DOUBLE":
                this.data_type = "Double";
                this.data_bef_len = Integer.valueOf(ary[3].split("/")[0]);
                this.data_aft_len = Integer.valueOf(ary[3].split("/")[1]);
                break;
            case "DATE":
                this.data_type = "Date";
                this.data_bef_len = 0;
                this.data_aft_len = 0;
                break;
            default: this.data_type = "";
                this.data_bef_len = 0;
                this.data_aft_len = 0;
        }

        if(ary.length > 4 && ary[4] != null && !ary[4].isEmpty() && ary[4].equalsIgnoreCase("PK")){
            this.pk_yn = true;
        }else{
            this.pk_yn = false;
        }
    }

    public String getEng_nm() {
        return eng_nm;
    }

    public void setEng_nm(String eng_nm) {
        this.eng_nm = eng_nm;
    }

    public String getEng_nm_cm() {
        return eng_nm_cm;
    }

    public void setEng_nm_cm(String eng_nm_cm) {
        this.eng_nm_cm = eng_nm_cm;
    }

    public String getHan_nm() {
        return han_nm;
    }

    public void setHan_nm(String han_nm) {
        this.han_nm = han_nm;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public Integer getData_bef_len() {
        return data_bef_len;
    }

    public void setData_bef_len(Integer data_bef_len) {
        this.data_bef_len = data_bef_len;
    }

    public Integer getData_aft_len() {
        return data_aft_len;
    }

    public void setData_aft_len(Integer data_aft_len) {
        this.data_aft_len = data_aft_len;
    }

    public Boolean getPk_yn() {
        return pk_yn;
    }

    public void setPk_yn(Boolean pk_yn) {
        this.pk_yn = pk_yn;
    }

    @Override
    public String toString() {
        return "colInfo{" +
                "eng_nm='" + eng_nm + '\'' +
                ", eng_nm_cm='" + eng_nm_cm + '\'' +
                ", han_nm='" + han_nm + '\'' +
                ", data_type='" + data_type + '\'' +
                ", data_bef_len=" + data_bef_len +
                ", data_aft_len=" + data_aft_len +
                ", pk_yn=" + pk_yn +
                '}';
    }
}