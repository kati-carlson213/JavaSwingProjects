public class Outputs {
    private String output = "";

    private boolean exceptionTrue;

    public void setOutput(String input, boolean isException) {
        exceptionTrue = isException;
        output=input;
    }

    public String getOutput() {

        if (exceptionTrue) {
            output= "true+" + output;
        }
        else {
            output="false" + output;
        }

        return output;
    }



}
