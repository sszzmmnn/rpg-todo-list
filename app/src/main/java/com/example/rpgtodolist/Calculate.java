package com.example.rpgtodolist;

import java.util.Calendar;
import java.util.Random;

public class Calculate {

    public static int strength(int pts) {
        float s = 1f + ((float) pts - 1f) / 4f;
        return (int)s;
    }

    public static float agility(int pts) {
        float a = 24f / (1f + ((float) pts - 1f) / 4f);
        return a;
    }

    public static float intl(int pts) {
        float i = 100f - (100f / (1f + (((float) pts - 1f) / 75f)));
        return i;
    }

    public static boolean crit(float cc) {
        Random random = new Random();
        double generatedPercentage = random.nextDouble() * 100;
        System.out.println(generatedPercentage <= cc ?
                String.format("Yes! %.2f / %.2f", generatedPercentage, cc) :
                String.format("No! %.2f / %.2f", generatedPercentage, cc));
        return generatedPercentage <= cc;
    }

    public static long newHitTime(long unixTimestamp, int agilityPts) {
        float time = agility(agilityPts)*3600;
        long res = unixTimestamp + (int)time;
        long currentTime = System.currentTimeMillis() / 1000;

        //Nadgonienie o 10000s żeby while() niżej za długo nie trwał
        if(res < currentTime - 10000) {
            while(res < currentTime - 10000) {
                res += 48200;
            }
        }
        while(res < currentTime) {
            res += (int)time;
        }
        return res;
    }

    public static String hitTimeCountdown(long secs) {
        long hrs = secs / (60 * 60);
        secs %= (60 * 60);
        long mins = secs / 60;
        secs %= 60;

        StringBuilder timeStringBuilder = new StringBuilder();
        int displayCount = 0;

        if (hrs > 0 && displayCount < 2) {
            timeStringBuilder.append(hrs).append("h ");
            displayCount++;
        }
        if (mins > 0 && displayCount < 2) {
            timeStringBuilder.append(mins).append("min ");
            displayCount++;
        }
        if (secs > 0 && displayCount < 2) {
            timeStringBuilder.append(secs).append("s");
            displayCount++;
        }

        return timeStringBuilder.toString();
    }

    public static int newEnemyHP(int startHP, int killCount) {
        return (int)(startHP + startHP * (0.1f * killCount));
    }
}
