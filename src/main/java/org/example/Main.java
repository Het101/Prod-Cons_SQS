package org.example;
public class Main {
    static final String sq = "SourceQueue";
    static final String dq = "DestinationQueue";
    public static void main(String[] args) {
        Consumer co = new Consumer(dq,sq);
        co.ConsumeMethod();
    }
}