package FirstSpring.utils;

public class StringArgument {
    public static boolean isEmpty(String param){
        return param==null||param.length()==0;
    }

    public static boolean isNotEmpty(String param){
        return !isEmpty(param);
    }
    public static boolean StringMaxLengthArgument(String param,int maxLength){
        return isNotEmpty(param)&&param.length()<maxLength;
    }
}
