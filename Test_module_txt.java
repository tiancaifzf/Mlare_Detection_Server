import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by max-fzf on 17-4-9.
 */
public class Test_module_txt {
    public static String name;
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {

        ArrayList<String> Permission_list = Collection_List();

        // double result_prob = svm.svm_predict_probability(model, test, l);
        name=args[0];
        //int test_data[] = Get_Result(Permission_list, Handle_permission("/home/user/FZF/cleanui.txt"));
        int test_data[] = Get_Result(Permission_list, Handle_permission("/home/user/Server/Up_Load_txt_step1/"+name+".txt"));
        svm_node[] test = new svm_node[Permission_list.size()];
        svm_model model = svm.svm_load_model("svm_model_file");
        if(test_data==null){
            System.out.println("txt have some problem....");
        }
        else
        {
            System.out.println("开始验证模型...");
            for (int j = 0; j < Permission_list.size(); j++) {
                test[j] = new svm_node();
                test[j].index = j + 1;
                test[j].value = test_data[j];
            }
            double result_normal = svm.svm_predict(model, test);
            double[] l = new double[2];
            double result_prob = svm.svm_predict_probability(model, test, l);
            System.out.println("================================");
            if(result_normal==0.0){
                System.out.println("APK没问题!");
            }
            else {
                System.out.println("APK怪怪的!");
            }
            System.out.println("概率： " + l[0] + "\t"+"\t" + l[1]);
            System.out.println("================================");
            Output_JSON(l[0],l[1]);
        }
    }
    @SuppressWarnings("unchecked")
    public static int[] Get_Result(ArrayList list, String path_name) throws IOException {
        int[] result = new int[list.size()];
        LinkedList linkedList = new LinkedList(list);
        // for (int i = 1; i <= 10000; i++) {
        //String number = String.valueOf(i);
        String file_path = path_name;
        if (file_path == null) {
            System.out.println("大大!出问题啦!......");
        } else
        {
            File filename = new File(file_path);
            if (filename.exists() == false) {
                System.out.println("TXT" + " do not exsist!!!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String buff;
            while ((buff = reader.readLine()) != null) {
                int a = linkedList.indexOf(buff);
                // System.out.println("Index="+a);
                if (a != -1) {
                    //System.out.println("匹配成功！");
                    result[a] = 1;
                }
            }
            reader.close();
            // ThreadTest threadTest=new ThreadTest(result,number);
            //     threadTest.run();
            // }
            return result;
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public static String Handle_permission(String path_name) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            String path_after = "/home/user/Server/Up_Load_txt_step2/"+name+".txt";
            File filename_Good = new File(path_name);
            if (filename_Good.exists()) {
                System.out.println("Start Handle_permission....");
                //好文件处理
                BufferedReader reader = new BufferedReader(new FileReader(filename_Good));
                String buff;
                File writename = new File(path_after);
                writename.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                while ((buff = reader.readLine()) != null) {
                    String[] arrays = buff.split("\\s+");
                    for (int j = 0; j < arrays.length; j++) {
                        if (Character.isLowerCase(arrays[j].charAt(0)) || arrays[j].equals("ACCESS") ||
                                arrays[j].equals("RECIVER") || arrays[j].equals("UPDATE") || arrays[j].equals("UPDATE")
                                || arrays[j].equals("BROADCAST") || arrays[j].equals("INTERNAL") || arrays[j].equals("DATA") || arrays[j].equals("STATUS")) {
                            System.out.println("忽略小写字母：" + arrays[j]);

                        } else {
                            // System.out.println("重新写入权限：" + arrays[j]);
                            out.write(arrays[j] + "\n");
                            out.flush();
                        }
                    }
                }
                out.close();
                reader.close();
                return path_after;
            } else {
                System.out.println(filename_Good.toString() + "不存在！！");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public static ArrayList Collection_List() throws IOException {
        int count = 0;
        ArrayList<String> Permission_list = new ArrayList<String>();
        String path = "/home/user/Server/Final_Good_10000/";
        File file = new File(path);
        int file_count = file.listFiles().length;
        //处理好的数据
        for (int i = 1; i <= file_count; i++) {
            String number = String.valueOf(i);
            String pathname = path + number + ".txt";
            File filename = new File(pathname);
            if (filename.exists() == false) {
                System.out.println(number + ".txt" + " do not exsist!!!");
                continue;
            }
            //    System.out.println(pathname);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String buff;
            while ((buff = reader.readLine()) != null) {
                String[] arrays = buff.split("\\s+");
                for (int j = 0; j < arrays.length; j++) {
                    if (Character.isLowerCase(arrays[j].charAt(0))) {
                        // System.out.println("小写字母：" + arrays[j] + "不加入");
                    } else {
                        if (!Permission_list.contains(arrays[j])) {
                            Permission_list.add(arrays[j]);
                            // System.out.println("已添加:" + arrays[j]);
                        } else {
                            count++;
                        }
                    }
                }
            }
            reader.close();
        }

        System.out.println("好的数据一共有：" + count + "重复");
        count = 0;
        //处理坏的数据
        String path_bad = "/home/user/Server/Final_Bad_10000/";
        File file_bad = new File(path);
        int file_count_bad = file.listFiles().length;
        for (int i = 1; i <= file_count_bad; i++) {
            String number = String.valueOf(i);
            String pathname = path_bad + number + ".txt";
            File filename = new File(pathname);
            if (filename.exists() == false) {
                System.out.println(number + ".txt" + " do not exsist!!!");
            }
            //    System.out.println(pathname);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String buff;
            while ((buff = reader.readLine()) != null) {
                String[] arrays = buff.split("\\s+");
                for (int j = 0; j < arrays.length; j++) {
                    if (Character.isLowerCase(arrays[j].charAt(0))) {
                        // System.out.println("小写字母：" + arrays[j] + "不加入");
                    } else {
                        if (!Permission_list.contains(arrays[j])) {
                            Permission_list.add(arrays[j]);
                            //System.out.println("已添加:" + arrays[j]);
                        } else {
                            count++;
                        }
                    }
                }
            }
            reader.close();
        }
        System.out.println("坏的数据一共有：" + count + "重复");
        return Permission_list;
    }
    public static void Output_JSON(double Good_probability,double Bad_probability) throws IOException {
        File writename=new File("/home/user/Server/TXT_Result/"+name+".json");
        writename.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        String objectToReturn = "{"+"\""+"Good_probability"+"\""+":"+"\""+String.valueOf(Good_probability)+"\""+","+"\""+"Bad_probability"+"\""+":"+"\""+String.valueOf(Bad_probability)+"\""+"}";
        System.out.println(objectToReturn);
        out.write(objectToReturn);
        out.flush();
        out.close();
    }
}
