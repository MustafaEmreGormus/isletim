package com.uni.scheduler.io;

import com.uni.scheduler.model.Proc;
import java.io.*;
import java.util.*;  

public class CSVLoader {
    public static List<Proc> load(String path) throws IOException {
        List<Proc> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] p = line.split(",");
                String id = p[0].trim();
                int arr = Integer.parseInt(p[1].trim());
                int burst = Integer.parseInt(p[2].trim());
                String pr = p[3].trim().toLowerCase();
                int priority = pr.equals("high") ? 1 : pr.equals("normal") ? 2 : 3;
                list.add(new Proc(id, arr, burst, priority));
            }
        }
        return list;
    }
}
