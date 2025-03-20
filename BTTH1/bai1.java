import java.util.Scanner;
import java.util.Random;

public class bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập bán kính r: ");
        double r = scanner.nextDouble();
        
        // Số điểm để tạo xấp xỉ
        int totalPoints = 1000000;
        int pointsInsideCircle = 0;
        
        Random random = new Random();
        
        // Sử dụng phương pháp Monte Carlo để xấp xỉ π
        for (int i = 0; i < totalPoints; i++) {
            // Tạo điểm ngẫu nhiên trong hình vuông [-r, r] × [-r, r]
            double x = (2 * random.nextDouble() - 1) * r;
            double y = (2 * random.nextDouble() - 1) * r;
            
            // Kiểm tra xem điểm có nằm trong hình tròn không
            if (x * x + y * y <= r * r) {
                pointsInsideCircle++;
            }
        }
        
        // Tỉ lệ điểm trong hình tròn so với tổng số điểm là π/4
        double pi = 4.0 * pointsInsideCircle / totalPoints;
        
        // Diện tích hình tròn = π * r^2
        double area = pi * r * r;
        
        System.out.println("Giá trị π xấp xỉ: " + pi);
        System.out.println("Diện tích hình tròn bán kính " + r + " là: " + area);
        
        scanner.close();
    }
}