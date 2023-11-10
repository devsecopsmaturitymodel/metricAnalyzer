package com.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class App 
{
    public static void main( String[] args )
    {
        // TODO: In separate class
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cat", "/local_repository/configuration.yaml");

        try {
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
