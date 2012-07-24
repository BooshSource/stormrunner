package com.templar.games.stormrunner.util;

import java.io.IOException;
import java.io.InputStream;

public class MonitoredInputStream extends InputStream
{
  ProgressListener pl;
  InputStream wrapped;

  public MonitoredInputStream(InputStream paramInputStream, ProgressListener paramProgressListener)
  {
    this.wrapped = paramInputStream;
    this.pl = paramProgressListener;
  }

  public int read() throws IOException {
    int i = this.wrapped.read();
    if (i != -1)
      addBytesRead(1L);
    return i;
  }

  public int read(byte[] paramArrayOfByte) throws IOException {
    int i = this.wrapped.read(paramArrayOfByte);
    if (i != -1)
      addBytesRead(i);
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
    int i = this.wrapped.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i != -1)
      addBytesRead(i);
    return i;
  }

  public long skip(long paramLong) throws IOException {
    long l = this.wrapped.skip(paramLong);
    addBytesRead(l);
    return l;
  }

  private void addBytesRead(long paramLong) {
    if (this.pl != null)
      this.pl.notifyProgress((int)paramLong);
  }

  public int available() throws IOException {
    return this.wrapped.available();
  }

  public void close() throws IOException {
    this.wrapped.close();
  }

  public void mark(int paramInt) {
    this.wrapped.mark(paramInt);
  }

  public void reset() throws IOException {
    this.wrapped.reset();
  }

  public boolean markSupported() {
    return this.wrapped.markSupported();
  }
}