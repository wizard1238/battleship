public class classA implements Test {
    public void recievedText(String text) {
        System.out.println(text);
    }

    public static void main(String[] args) {
        classA classa = new classA();
        classB classb = new classB(classa);
        classb.testInterfaceThingy();
    }
}
