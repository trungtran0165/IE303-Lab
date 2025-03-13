package BTTH1;

import java.util.Random;

public class bai2 {
    public static void main(String[] args) {
        // Số điểm để xấp xỉ (càng lớn càng chính xác)
        int totalPoints = 10000000;
        int pointsInsideCircle = 0;

        Random random = new Random();

        System.out.println("Đang xấp xỉ π sử dụng phương pháp Monte Carlo với " + totalPoints + " điểm...");

        // Sử dụng phương pháp Monte Carlo
        for (int i = 0; i < totalPoints; i++) {
            // Tạo điểm ngẫu nhiên trong hình vuông [-1, 1] × [-1, 1]
            double x = 2.0 * random.nextDouble() - 1.0;
            double y = 2.0 * random.nextDouble() - 1.0;

            // Kiểm tra xem điểm có nằm trong đường tròn đơn vị
            if (x * x + y * y <= 1.0) {
                pointsInsideCircle++;
            }
        }

        // Tính xấp xỉ π
        double piApproximation = 4.0 * pointsInsideCircle / totalPoints;

        System.out.println("Giá trị xấp xỉ của π: " + piApproximation);
        System.out.println("Giá trị thực của π:    " + Math.PI);
        System.out.println("Sai số: " + Math.abs(piApproximation - Math.PI));
    }
}