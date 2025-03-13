package BTTH1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class bai3 {
    static class Point {
        double x, y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập số lượng trạm phát sóng: ");
        int n = scanner.nextInt();
        
        Point[] stations = new Point[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Nhập tọa độ x của trạm " + (i + 1) + ": ");
            double x = scanner.nextDouble();
            System.out.print("Nhập tọa độ y của trạm " + (i + 1) + ": ");
            double y = scanner.nextDouble();
            stations[i] = new Point(x, y);
        }
        
        List<Point> alertStations = findAlertStations(stations);
        
        System.out.println("\nCác trạm cảnh báo:");
        for (Point station : alertStations) {
            System.out.println(station);
        }
        
        scanner.close();
    }
    
    // Tìm các trạm cảnh báo dựa trên thuật toán Convex Hull
    public static List<Point> findAlertStations(Point[] stations) {
        if (stations.length <= 3) {
            return Arrays.asList(stations);
        }
        
        return grahamScan(stations);
    }
    
    // Thuật toán Graham Scan để tìm bao lồi (convex hull)
    public static List<Point> grahamScan(Point[] points) {
        // Tìm điểm có tọa độ y nhỏ nhất (nếu có nhiều điểm thì lấy điểm có x nhỏ nhất)
        Point anchor = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].y < anchor.y || (points[i].y == anchor.y && points[i].x < anchor.x)) {
                anchor = points[i];
            }
        }
        
        // Sắp xếp các điểm theo góc so với điểm anchor
        final Point finalAnchor = anchor;
        Arrays.sort(points, (p1, p2) -> {
            if (p1 == finalAnchor) return -1;
            if (p2 == finalAnchor) return 1;
            
            double angle1 = Math.atan2(p1.y - finalAnchor.y, p1.x - finalAnchor.x);
            double angle2 = Math.atan2(p2.y - finalAnchor.y, p2.x - finalAnchor.x);
            
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            
            // Nếu góc bằng nhau, lấy điểm xa hơn
            double dist1 = Math.sqrt(Math.pow(p1.x - finalAnchor.x, 2) + Math.pow(p1.y - finalAnchor.y, 2));
            double dist2 = Math.sqrt(Math.pow(p2.x - finalAnchor.x, 2) + Math.pow(p2.y - finalAnchor.y, 2));
            
            return Double.compare(dist2, dist1);
        });
        
        // Thực hiện thuật toán Graham Scan
        Stack<Point> hull = new Stack<>();
        hull.push(points[0]);
        hull.push(points[1]);
        
        for (int i = 2; i < points.length; i++) {
            while (hull.size() > 1 && !isLeftTurn(hull.get(hull.size() - 2), hull.peek(), points[i])) {
                hull.pop();
            }
            hull.push(points[i]);
        }
        
        return new ArrayList<>(hull);
    }
    
    // Kiểm tra nếu ba điểm tạo thành một góc trái
    private static boolean isLeftTurn(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
    }
}