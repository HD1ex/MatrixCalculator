/**
 * This class represents a matrix
 *
 * @author Alexander Sommer
 * @since 09.01.2018
 */
public class Matrix {
    private double[][] fields;
    private int p;
    private int q;

    public Matrix(int p, int q) {
        fields = new double[p][q];
        this.p = p;
        this.q = q;
    }

    public Matrix(Matrix from) {
        fields = new double[p][q];
        this.p = from.p;
        this.q = from.q;

        for (int i = 0; i < fields.length; i++) {
            System.arraycopy(from.fields[i], 0, this.fields[i], 0, fields[i].length);
        }
    }

    public double get(int p, int q) {
        return fields[p][q];
    }

    public void set(int p, int q, double value) {
        if (p >= this.p || q >= this.q || p < 0 || q < 0)
            return;

        fields[p][q] = value;
    }

    public void setIdentity() {
        for (int x = 0; x < q; x++) {
            for (int y = 0; y < p; y++) {
                set(y, x, x == y ? 1 : 0);
            }
        }
    }

    public int getHeight() {
        return p;
    }

    public int getWidth() {
        return q;
    }

    public Matrix multiply(Matrix other) {
        int colsLeft = this.getWidth();
        int rowsLeft = this.getHeight();
        int colsRight = other.getWidth();
        int rowsRight = other.getHeight();

        if (colsLeft != rowsRight) {
            return null;
        }

        double[][] A = this.fields;
        double[][] B = other.fields;

        Matrix mat = new Matrix(rowsLeft, colsRight);

        for (int i = 0; i < rowsLeft; i++) {
            for (int j = 0; j < colsRight; j++) {
                double value = 0;

                for (int k = 0; k < colsLeft; k++) {
                    value += A[i][k] * B[k][j];
                }

                mat.set(i, j, value);
            }
        }

        return mat;
    }

    public static Matrix solve(Matrix matrix) {
        //Select a column != 0
        int col = -1;
        for (int x = 0; x < matrix.q; x++) {
            for (int y = 0; y < matrix.p; y++) {
                if (matrix.fields[y][x] != 0) {
                    col = x;
                    break;
                }
            }
            if (col >= 0)
                break;
        }
        if (col < 0)
            return matrix;

        //Swap if 0
        if (matrix.fields[0][col] == 0) {
            for (int i = 0; i < matrix.p; i++) {
                if (matrix.fields[i][0] != 0) {
                    double[] curRow = matrix.getRow(0);
                    double[] otherRow = matrix.getRow(i);

                    matrix.setRow(0, otherRow);
                    matrix.setRow(i, curRow);

                    break;
                }
            }
        }

        //Divide
        for (int i = 0; i < matrix.q; i++) {
            matrix.fields[0][i] /= matrix.fields[0][col];
        }

        return matrix;
    }

    public Matrix solve() {
        return solve(new Matrix(this));
    }

    private void setRow(int n, double[] row) {
        System.arraycopy(row, 0, fields[n], 0, q);
    }


    public double[] getRow(int n) {
        if (n >= p || n < 0) {
            return null;
        }

        double[] row = new double[q];

        for (int i = 0; i < q; i++) {
            row[i] = get(n, i);
        }

        return row;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Top
        sb.append("\u250F");
        for (int i = 0; i < 11; i++) {
            //sb.append("\u2501");
            sb.append(" ");
        }
        sb.append("\u2513\n");

        //Mid
        for (int i = 0; i < p; i++) {
            sb.append("\u2503 ");
            for (int j = 0; j < q; j++) {
                sb.append(fields[i][j]);
                sb.append(" ");
            }
            sb.append("\u2503\n");
        }

        //Bot
        sb.append("\u2517");
        for (int i = 0; i < 11; i++) {
            //sb.append("\u2501");
            sb.append(" ");
        }
        sb.append("\u251B\n");

        return sb.toString();
    }
}
