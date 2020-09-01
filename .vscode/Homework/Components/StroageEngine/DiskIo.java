package Homework.Components.StroageEngine;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DiskIo {
    public void Write() throws IOException;
    public void Read() throws FileNotFoundException, CloneNotSupportedException;
}