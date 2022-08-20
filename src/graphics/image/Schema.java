package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    @Override
    public char convert(int color) {
        if (color < 32 && color >= 0) {
            return '#';
        } else if (color < 64 && color >= 32){
            return '$';
        } else if (color < 96 && color >= 64){
            return '@';
        } else if (color < 128 && color > 96){
            return '%';
        } else if (color < 160 && color >=128){
            return '*';
        } else if (color < 192 && color >= 160){
            return '+';
        } else if (color < 224 && color >= 192){
            return '-';
        } else if (color <= 257 && color >= 224){
            return '\'';
        } else {
            return ' ';
        }
    }
}
