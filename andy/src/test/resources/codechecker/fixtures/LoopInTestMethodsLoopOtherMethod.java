package delft;

public class LoopInTestMethodsLoopOtherMethod {

    void t1() {
        int i = 10;
        while(i < 10) {
            
        }
    }

    void t2() {
        int i = 10;
        do {

        } while(i < 10);
    }

    void t3() {
        for(int i = 0; i < 10; i++) {

        }
    }

    void t4() {
        int[] x = new int[] { 10, 10, 10 };
        for(int i : x) {

        }
    }

}
