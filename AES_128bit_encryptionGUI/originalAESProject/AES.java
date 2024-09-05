//NOTE: this is an implementation of 128-bit key AES in ECB mode.

import java.util.*;
import java.io.*;

class AES {

    //XOR function for two binary strings
    public static String XOR(String one, String two) {
        String result="";
        for (int j=0; j<one.length(); j++) {
            if (one.charAt(j)==two.charAt(j)) {
                result+='0';
            }
            else {
                result+='1';
            }
        }

        return result;
    }

    //function for finding the remainder when doing mod 2 division of binary numbers
    public static String gfRemainder(String one, String two) {
        String temp="";
        String temp2="";


        int diff=0;


        if (two.length()==one.length()) {
            temp=XOR(one, two);
        }


        if (two.length()>one.length()) {
            temp2=one;
            one=two;
            two=temp2; }


        if (two.length()!=one.length()) {
            diff = one.length()-two.length()+1;


            temp = one.substring(0, two.length());
            temp2= one.substring(two.length(), one.length());

            temp = XOR(temp, two);
            temp=temp+temp2;



            while (diff!=0) {
                String zero="";

                int index = temp.indexOf("1");
                temp=temp.substring(index, temp.length());


                if (two.length()==temp.length()) {
                    temp=XOR(temp, two);  }

                if (temp.length()>two.length()) {

                    for (int i=0; i<temp.length()-two.length(); i++) {
                        zero+="0";
                    }

                    temp2=two+zero;
                    zero="";

                    temp = XOR(temp, temp2);

                }

                diff--;
            }

        }

        int index = temp.indexOf("1");
        temp=temp.substring(index);

        return temp;
    }



//function for XORing each entry of two matrices with each other

    public static int[][] xorMatrix (int[][] state, int[][] key) {

        int[][] newArr= new int[4][4];


        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                String one=Integer.toBinaryString(state[i][j]);
                String two=Integer.toBinaryString(key[i][j]);

                String zero="";

                if (one.length()<8) {
                    for (int x =0; x<8-one.length(); x++) {
                        zero+="0";
                    }
                    one=zero+one;
                }

                zero="";

                if (two.length()<8) {
                    for (int x =0; x<8-two.length(); x++) {
                        zero+="0";
                    }
                    two=zero+two;

                }

                zero="";

                newArr[i][j] = Integer.parseInt((XOR(one, two)), 2);
            }
        }

        return newArr;

    }



//arrays for galois field matrix, sbox matrix, and rcon

    static int[][] galois = {{0x02, 0x03, 0x01, 0x01},
            {0x01, 0x02, 0x03, 0x01},
            {0x01, 0x01, 0x02, 0x03},
            {0x03, 0x01, 0x01, 0x02}};



    static int[][] sbox = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};

    static int[] rCon = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};

    //arrays for inverse versions

    static int[][] invGalois = {{0x0e, 0x0b, 0x0d, 0x09},
            {0x09, 0x0e, 0x0b, 0x0d},
            {0x0d, 0x09, 0x0e, 0x0b},
            {0x0b, 0x0d, 0x09, 0x0e}};

    static int[][] invSbox = {{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb}, {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb}, {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e}, {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25}, {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92}, {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84}, {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06}, {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b}, {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73}, {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e}, {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b}, {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4}, {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f}, {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef}, {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61}, {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};



    //subbytes function
    public static int[][] subBytes(int[][] arr) {

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)    {

                String str = Integer.toHexString(arr[i][j]);

                if (str.length()!=2){
                    str= "0" + str;
                }


                String temp1= String.valueOf(str.charAt(0));
                String temp2 = String.valueOf(str.charAt(1));



                int num1 = Integer.parseInt(temp1, 16);
                int num2 = Integer.parseInt(temp2, 16);

                arr[i][j] = sbox[num1][num2];

            }

        }

        return arr;
    }




    //shift rows function
    public static int[][] shiftRows(int[][] arr) {
        for (int i = 1; i < arr.length; i++) {

            int [] tempArr = arr[i];

            int times=i;

            while (times > 0) {
                int temp = tempArr[0];
                for (int x = 0; x < tempArr.length - 1; x++) {
                    tempArr[x] = tempArr[x + 1];
                }
                tempArr[tempArr.length - 1] = temp;
                --times;
            }
        }
        return arr;
    }




    //mix Columns function
    public static int[][] mixColumns(int[][]arr){
        int result[][]=new int[4][4];
        String []tempArr=new String[4];

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                result[i][j]=0;

                String ans="";

                for(int x=0;x<4;x++) {
                    String bValue="";

                    int gfNum = galois[i][x];
                    if (gfNum==1) {
                        bValue=Integer.toBinaryString(arr[x][j]);
                    }
                    else if (gfNum==2) {
                        bValue=Integer.toBinaryString(arr[x][j])+"0";

                    }
                    else if (gfNum==3) {
                        bValue=Integer.toBinaryString(arr[x][j])+"0";
                        String bValue2=Integer.toBinaryString(arr[x][j]);

                        int diff = bValue.length()-bValue2.length();
                        if (diff>0) {
                            String zero="";
                            for (int h=0; h<diff; h++) {
                                zero+="0";
                            }
                            bValue2=zero+bValue2;
                        }
                        bValue= XOR(bValue2, bValue);
                    }




                    if (bValue.length()>8) {
                        bValue = gfRemainder(bValue, "100011011");
                    }
                    if (bValue.length()<8) {
                        int diff = 8-bValue.length();
                        String zero="";

                        for (int v=0; v<diff; v++) {
                            zero+="0";
                        }

                        bValue=zero+bValue;

                    }

                    tempArr[x]= bValue;

                }


                for (int b=0;  b<8; b++) {

                    int number =Character.getNumericValue(tempArr[0].charAt(b)) + Character.getNumericValue(tempArr[1].charAt(b)) + Character.getNumericValue(tempArr[2].charAt(b)) + Character.getNumericValue(tempArr[3].charAt(b));

                    if(number%2==0) {
                        ans+="0";
                    }
                    else {ans+="1";}


                }

                result[i][j]=Integer.parseInt(ans,2);
            }
        }

        return result;

    }


    //invSubbytes function
    public static int[][] invSubBytes(int[][] arr) {

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)    {

                String str = Integer.toHexString(arr[i][j]);

                if (str.length()!=2){
                    str= "0" + str;
                }


                String temp1= String.valueOf(str.charAt(0));
                String temp2 = String.valueOf(str.charAt(1));



                int num1 = Integer.parseInt(temp1, 16);
                int num2 = Integer.parseInt(temp2, 16);

                arr[i][j] = invSbox[num1][num2];

            }

        }

        return arr;
    }





    //inverse shift rows function
    public static int[][] invShiftRows(int[][] arr) {

        for (int i = 1; i < arr.length; i++) {

            int [] tempArr = arr[i];

            int times=i;

            while (times > 0) {
                int temp = tempArr[tempArr.length-1];
                for (int x = tempArr.length - 1; x>0; x--) {
                    tempArr[x] = tempArr[x - 1];
                }
                tempArr[0] = temp;
                --times;
            }
        }
        return arr;
    }




    //inv MixColumns function
    public static int[][] invMixColumns(int[][]arr){
        int result[][]=new int[4][4];
        String []tempArr=new String[4];

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                result[i][j]=0;

                String ans="";

                for(int x=0;x<4;x++) {
                    String bValue="";

                    int gfNum = invGalois[i][x];

                    if (gfNum==9) {
                        bValue="000" + Integer.toBinaryString(arr[x][j]);
                        bValue= XOR(bValue, (Integer.toBinaryString(arr[x][j]) + "000"));

                    }

                    else if (gfNum==11) {
                        bValue="0" + Integer.toBinaryString(arr[x][j]);
                        bValue= XOR(bValue, (Integer.toBinaryString(arr[x][j]) + "0"));
                        bValue="00"+ bValue;

                        bValue= XOR(bValue, (Integer.toBinaryString(arr[x][j]) + "000"));
                    }

                    else if (gfNum==13) {
                        bValue="00" + Integer.toBinaryString(arr[x][j]);
                        bValue= XOR(bValue,(Integer.toBinaryString(arr[x][j]) +"00"));
                        bValue = "0" + bValue;
                        bValue= XOR(bValue, (Integer.toBinaryString(arr[x][j]) + "000"));


                    }
                    else if (gfNum==14) {
                        bValue= "0" + Integer.toBinaryString(arr[x][j]) + "0";
                        bValue=XOR(bValue, (Integer.toBinaryString(arr[x][j])+ "00"));
                        bValue= "0" + bValue;
                        bValue= XOR(bValue, (Integer.toBinaryString(arr[x][j]) + "000"));

                    }




                    if (bValue.length()>8) {
                        bValue = gfRemainder(bValue, "100011011");
                    }
                    if (bValue.length()<8) {
                        int diff = 8-bValue.length();
                        String zero="";

                        for (int v=0; v<diff; v++) {
                            zero+="0";
                        }

                        bValue=zero+bValue;

                    }

                    tempArr[x]= bValue;

                }


                for (int b=0;  b<8; b++) {

                    int number =Character.getNumericValue(tempArr[0].charAt(b)) + Character.getNumericValue(tempArr[1].charAt(b)) + Character.getNumericValue(tempArr[2].charAt(b)) + Character.getNumericValue(tempArr[3].charAt(b));

                    if(number%2==0) {
                        ans+="0";
                    }
                    else {ans+="1";}


                }

                result[i][j]=Integer.parseInt(ans,2);
            }
        }

        return result;

    }




    //function for getting each round's round key/the key schedule
    public static int[][] getRoundKey(int[][] twoArr, int n) {
        int[][] resultArr= new int[4][4];

        int[] arr2= twoArr[3];
        int[] arr=  Arrays.copyOf(arr2, 4);


        int one = arr2[0];
        for(int j = 0; j < arr2.length-1; j++){

            arr2[j] = arr2[j+1];
        }

        arr2[3] = one;


        String str="";


        for (int i=0; i<arr2.length; i++) {
            str = Integer.toHexString(arr2[i]); }


        if (str.length()!=2){
            str= "0" + str;
        }



        for (int i=0; i<arr2.length; i++) {
            str = Integer.toHexString(arr2[i]);


            if (str.length()<=1) {
                if (str.length()==0) {
                    str="00" + str;
                }
                if (str.length()==1) {
                    str= "0" + str;
                }
            }




            String temp1= String.valueOf(str.charAt(0));
            String temp2 = String.valueOf(str.charAt(1));


            int num1 = Integer.parseInt(temp1, 16);
            int num2 = Integer.parseInt(temp2, 16);


            arr2[i] = sbox[num1][num2];


        }


        String constant = Integer.toBinaryString(rCon[n]);


        String zero="";

        if (constant.length()<8) {
            for (int i=0; i<8-constant.length(); i++) {
                zero+="0";
            }
            constant=zero+constant;
        }

        zero="";

        String binNum= Integer.toBinaryString(arr2[0]);



        if (binNum.length()<8) {
            for (int i=0; i<8-binNum.length(); i++) {
                zero+="0";
            }
            binNum=zero+binNum;
        }



        String addRcon= XOR(binNum, constant);



        int r =Integer.parseInt(addRcon,2);
        arr2[0] = r;



        twoArr[3]=arr;


        for (int k=0; k<4; k++) {
            for (int i=0; i<4; i++) {

                String first =Integer.toBinaryString(twoArr[k][i]);
                String second =Integer.toBinaryString(arr2[i]);


                zero="";

                if (first.length()<8) {
                    for (int x=0; x<8-first.length(); x++) {
                        zero+="0";
                    }
                    first=zero+first;
                }



                zero="";

                if (second.length()<8) {
                    for (int y=0; y<8-second.length(); y++) {
                        zero+="0";
                    }
                    second=zero+second;
                }

                String result=XOR(first, second);

                resultArr[k][i] = Integer.parseInt(result,2);

                arr2[i] = resultArr[k][i];

            }

        }

        return resultArr;

    }



    //making a 2d array for a string input that is in hexadecimal
    public static int[][] getMatrix(int[][] state, String hexString) {
        String [] arr = hexString.split("(?<=\\G..)");

        for (int i=0; i<4; i++) {
            state[i][0] = Integer.parseInt(arr[i], 16);
            state[i][1] = Integer.parseInt(arr[(i+4)], 16);
            state[i][2] = Integer.parseInt(arr[(i+8)], 16);
            state[i][3] = Integer.parseInt(arr[(i+12)], 16);
        }

        return state;

    }


    //function for getting a string value from a 2d array
    public static String stringFromArr(int[][] arr) {
        String s="";
        String [] tempArr= new String[4];



        for (int i=0; i<4; i++) {
            int x=0;
            int y=0;
            String zero="0";

            String a = Integer.toHexString(arr[i][0]);
            String b = Integer.toHexString(arr[i][1]);
            String c = Integer.toHexString(arr[i][2]);
            String d = Integer.toHexString(arr[i][3]);


            if (a.length()<2) {
                a = zero+a;
            }
            if (b.length()<2) {
                b = zero+b;
            }
            if (c.length()<2) {
                c = zero+c;
            }
            if (d.length()<2) {
                d = zero+d;
            }


            tempArr[i] = a+b+c+d;



        }

        s = tempArr[0] + tempArr[1] + tempArr[2] + tempArr[3];

        return s;


    }


    public static String encrypt(String hexState, String hexKey) {
        //array to hold state matrix
        int[][] state = new int[4][4];

        //array to hold key0
        int[][] key = new int[4][4];

        //array to use for the keys in the loop
        int[][] tempKey = new int[4][4];

        //array to use for the roundkeys
        String[] roundKeys = new String[10];



        state = getMatrix(state, hexState);

        key= getMatrix(key, hexKey);



        //transpose function
        for(int i=0; i<key.length; i++) {
            for(int j=0; j<tempKey.length; j++)  {
                tempKey[j][i]=key[i][j];
            }
        }




        //calculates all the roundkeys and puts them in an array
        for (int x=0; x<roundKeys.length; x++) {
            tempKey= getRoundKey(tempKey, x);
            String s= stringFromArr(tempKey);

            roundKeys[x] =s;
        }



        //XORs plaintext with the orignal key before round 1 begins
        state = xorMatrix(state, key);



        //goes through each of the ten rounds of encryption
        for (int i=0; i<10; i++) {

            int [][] temp = new int[4][4];

            for(int x = 0; x < 4; x++) {
                temp[x] = Arrays.copyOf(state[x], 4);
            }

            int [][] temp2 =getMatrix(temp, roundKeys[i]);


            if (i!=9) {
                state= subBytes(state);
                state = shiftRows(state);
                state = mixColumns(state);

                state = xorMatrix(state, temp2);
            }

            if(i==9) {
                state= subBytes(state);
                state = shiftRows(state);

                state = xorMatrix(state, temp2);
            }

        }




        String ciphertext = "";

        //loop for making the ciphertext into a string
        for (int u=0; u<4; u++) {
            String zero="0";

            String one=Integer.toHexString(state[0][u]);
            String two=Integer.toHexString(state[1][u]);
            String three= Integer.toHexString(state[2][u]);
            String four=Integer.toHexString(state[3][u]);

            if (one.length()<2) {
                one=zero+one;
            }

            if (two.length()<2) {
                two=zero+two;
            }

            if (three.length()<2) {
                three=zero+three;
            }

            if (four.length()<2) {
                four=zero+four;
            }

            ciphertext+= one+two+three+four;
        }


        return ciphertext;

    }


    public static String decrypt(String hexState, String hexKey) {

        //array to hold state matrix
        int[][] state = new int[4][4];

        //array to hold key0
        int[][] key = new int[4][4];

        //array to use for the keys in the loop
        int[][] tempKey = new int[4][4];

        //array to use for the roundkeys
        String[] roundKeys = new String[10];



        state = getMatrix(state, hexState);

        key= getMatrix(key, hexKey);



        //transpose function
        for(int i=0; i<key.length; i++) {
            for(int j=0; j<tempKey.length; j++)  {
                tempKey[j][i]=key[i][j];
            }
        }




        //calculates all the roundkeys and puts them in an array
        for (int x=0; x<roundKeys.length; x++) {
            tempKey= getRoundKey(tempKey, x);
            String s= stringFromArr(tempKey);

            roundKeys[x] =s;
        }




        //goes through each of the rounds of decryption
        for (int i=9; i>-1; i--) {

            int [][] temp = new int[4][4];

            for(int x = 0; x < 4; x++) {
                temp[x] = Arrays.copyOf(state[x], 4);
            }




            int [][] temp2 =getMatrix(temp, roundKeys[i]);


            if(i==9) {

                state = xorMatrix(state, temp2);
                state = invShiftRows(state);
                state= invSubBytes(state);


            }


            else if (i<9) {
                state = xorMatrix(state, temp2);

                state = invMixColumns(state);
                state = invShiftRows(state);
                state= invSubBytes(state);

            }


        }

        state = xorMatrix(state, key);



        String plaintext = "";

        //loop for making the plaintext into a string
        for (int u=0; u<4; u++) {
            String zero="0";

            String one=Integer.toHexString(state[0][u]);
            String two=Integer.toHexString(state[1][u]);
            String three= Integer.toHexString(state[2][u]);
            String four=Integer.toHexString(state[3][u]);

            if (one.length()<2) {
                one=zero+one;
            }

            if (two.length()<2) {
                two=zero+two;
            }

            if (three.length()<2) {
                three=zero+three;
            }

            if (four.length()<2) {
                four=zero+four;
            }

            plaintext+= one+two+three+four;
        }


        return plaintext;



    }


    //turns text string to hex string
    public static String hexFromASCII(String s) {
        String result="";
        for (int i=0; i<s.length(); i++) {

            char c= s.charAt(i);
            int num = c;
            String hex = Integer.toHexString(num);

            if (hex.length()<2) {
                hex = "0" + hex;
            }

            result+=hex;

        }

        if (result.length()<32) {
            int length = (32 - result.length())/2;
            for (int j=0; j<length; j++) {
                result+= "20";
            }
        }



        int remain = result.length() % 16;


        if (remain!=0) {
            for (int j=-1; j< remain; j++) {
                result+= "20";
            }
        }



        return result;
    }


    //to turn hex string back to ASCII
    public static String hexToASCII(String str) {

        String result = "";

        for(int i=0;i<str.length();i+=2)
        {
            String s = str.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            result = result + (char) decimal;
        }

        return result;
    }

    //-------------------------------------------------------


    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        String text = "";

        boolean empty = false;

        try {
            File input = new File("input.txt");

            if (input.length() == 0) {
                empty=true;
                System.out.println("File is empty or doesn't exist. Try again.");}

            else {
                Scanner scan2 = new Scanner(input);
                while (scan2.hasNextLine()) {
                text += scan2.nextLine();
                }

                if ((text.length() % 16)!=0) {
                    int remainder = text.length() % 16;
                    while (remainder!=0) {
                        text+= " ";
                        remainder--;
                    }
                }

                scan2.close();
            }

        }  catch (FileNotFoundException e) {
            empty=true;
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



        if (empty==false) {


            //assumes user is inputting text/ASCII text.
            System.out.println("Input your key:");
            String key = scan.nextLine();


            //converts to hex string
            text = hexFromASCII(text);
            key = hexFromASCII(key);


            //takes the input and only takes the first 32 hex characters for the key
            key = key.substring(0, 32);


            int blockAmount = text.length() / 32;

            String[] blockArray = new String[blockAmount];


            //puts each block into an array
            for (int i = 0; i < blockAmount; i++) {
                int start = i * 32;
                int end = start + 32;

                blockArray[i] = text.substring(start, end);
            }



            //asks users to encrypt or decrypt
            System.out.println("Do you want to encrypt or decrypt? Please type 'e' or 'd'");
            String answer = scan.nextLine();

            while (!answer.equals("d") && !answer.equals("decrypt") && !answer.equals("e") && !answer.equals("encrypt")) {
                System.out.println("Not a valid input. Please try again. Do you want to encrypt or decrypt?");
                answer = scan.nextLine();
            }


            String result = "";


            //goes through all the blocks to encrypt or decrypt them
            for (int x = 0; x < blockArray.length; x++) {

                text = blockArray[x];
                if (answer.equals("e") || answer.equals("encrypt")) {
                    result += encrypt(text, key);
                } else if (answer.equals("d") || answer.equals("decrypt")) {
                    result += decrypt(text, key);
                }

            }


            result = hexToASCII(result);

            //makes output file with the encrypted or decrypted result
            try {
                File output = new File("output.txt");

                FileWriter writer = new FileWriter(output, false);

                if (output.createNewFile()) {
                    writer.write(result);
                    writer.close();
                    System.out.println("File created: " + output.getName());

                } else {
                    writer.write(result);
                    writer.close();
                }

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }


    }
}