import java.util.*;

/**
 * This class implements a command-line interface
 *
 * @author Alexander Sommer
 * @since 09.01.2018
 */

public class MatrixCalculator {
    private Map<String, Matrix> matrices = new HashMap<String, Matrix>();

    private MatrixCalculator() {
        for (int i = 1; i <= 5; i++) {
            Matrix matrix = new Matrix(i, i);
            matrix.setIdentity();
            matrices.put("ID" + i, matrix);
        }

        Matrix matrix = new Matrix(3, 4);
        matrix.set(0, 0, 1);
        matrix.set(1, 0, 4);
        matrix.set(2, 0, 9);

        matrix.set(0, 1, 1);
        matrix.set(1, 1, 2);
        matrix.set(2, 1, 3);

        matrix.set(0, 2, 1);
        matrix.set(1, 2, 1);
        matrix.set(2, 2, 1);

        matrix.set(0, 3, 0);
        matrix.set(1, 3, 1);
        matrix.set(2, 3, 3);

        matrices.put("A", matrix);
    }

    private void run() {
        System.out.println("Matrix Calculator V1.0");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            String[] columns = input.split(" ");

            switch (columns[0]) {
                case "q":
                case "quit":
                    return;
                case "def":
                case "define":
                    String defineError = "Please input a valid type to define (matrix, vector)";

                    if (columns.length <= 1) {
                        System.err.println(defineError);
                        break;
                    }
                    switch (columns[1]) {
                        default:
                            System.err.println(defineError);
                            break;
                        case "mat":
                        case "matrix":
                            readMatrix(columns, scanner);
                            break;
                        case "vec":
                        case "vector":
                            readVector(columns);
                            break;
                    }
                    break;
                case "print":
                    print(columns);
                    break;
                case "mul":
                case "multiply":
                    if (columns.length < 3) {
                        System.err.println("multiply needs 'matA matB' as arguments.");
                        break;
                    }
                    if (!matrices.containsKey(columns[1])) {
                        System.err.println("The first matrix doesn't exist");
                        break;
                    }
                    if (!matrices.containsKey(columns[2])) {
                        System.err.println("The second matrix doesn't exist");
                        break;
                    }

                    Matrix a = matrices.get(columns[1]);
                    Matrix b = matrices.get(columns[2]);
                    Matrix res = a.multiply(b);
                    if (res != null) {
                        String var = getFreeVariableName();
                        matrices.put(var, res);
                        System.out.printf("%s * %s -> %s\n", columns[1], columns[2], var);
                    } else {
                        System.err.println("The matrices can't be multiplied!");
                    }
                    break;

            }
        }
    }

    private void print(String[] columns) {
        if (columns.length == 1) {
            Set<String> mats = matrices.keySet();

            System.out.print("Matrices: ");
            for (String s : mats) {
                System.out.print(s + " ");
            }
            System.out.println();
            return;
        }
        if (columns.length != 2) {
            System.err.println();
            return;
        }
        String name = columns[1];


        Matrix matrix;

        if (matrices.containsKey(name)) {
            matrix = matrices.get(name);
        } else {
            System.err.println("Can't find object with name '" + name + "'");
            return;
        }

        System.out.println(matrix);
        /*
        int p = matrix.getHeight();
        int q = matrix.getWidth();

        for (int y = 0; y < p; y++) {
            System.out.print("   ");
            System.out.print(y == 0 ? "⌈" : y == p - 1 ? "⌊" : "|");
            for (int x = 0; x < q; x++) {
                System.out.print(matrix.get(y, x) + (x == q - 1 ? "" : "\t"));
            }
            System.out.println(y == 0 ? "⌉" : y == p - 1 ? "⌋" : "|");
        }
        */

    }

    private void readMatrix(String[] columns, Scanner scanner) {
        if (columns.length < 3) {
            System.err.println("\nCreate matrix either takes 'NxM' or 'NAME NxM' as additional arguments");
            System.err.println("Where N,M are integers and NAME is the name of the new matrix.");
            return;
        }
        //Find the name
        String matrixName;
        if (Character.isDigit(columns[2].toCharArray()[0])) {
            matrixName = getFreeVariableName();
        } else {
            matrixName = columns[2];
        }

        //Get the size of the new matrix
        int p, q;
        try {
            String[] nums = columns[columns.length - 1].split("x");
            if (nums.length != 2)
                throw new NumberFormatException();
            p = Integer.parseInt(nums[0]);
            q = Integer.parseInt(nums[1]);
        } catch (NumberFormatException ex) {
            System.err.println("\nThe matrix size should be inputted as 'PxQ' where P,Q are integers");
            return;
        }
        Matrix matrix = new Matrix(p, q);

        //System.out.print(matrixName + " = ⌈");

        int a = 0;
        while (a < p) {
            System.out.print("row " + (a + 1) + " = ");

            try {
                String[] nums = scanner.nextLine().split(" ");

                if (nums.length != q)
                    continue;

                for (int i = 0; i < q; i++) {
                    matrix.set(a, i, Double.parseDouble(nums[i]));
                }
            } catch (Exception ex) {
                continue;
            }
            a++;
        }
        matrices.put(matrixName, matrix);
        System.out.printf("-> %s\n", matrixName);
    }

    private void readVector(String[] columns) {

    }

    private void moveCursor(int row, int column) {
        char escCode = 0x1B;

        System.out.print(String.format("%c[%d;%df", escCode, row, column));
    }

    public static void main(String[] args) {
        new MatrixCalculator().run();
    }

    private String getFreeVariableName() {
        String matrixName = "A";
        while (matrices.containsKey(matrixName)) {
            char[] chars = matrixName.toCharArray();

            if (++chars[chars.length - 1] == 'Z') {
                matrixName += 'A';
            } else {
                matrixName = String.valueOf(chars);
            }
        }

        return matrixName;
    }
}
