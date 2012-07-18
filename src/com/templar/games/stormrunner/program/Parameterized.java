package com.templar.games.stormrunner.program;

public interface Parameterized
{
  public abstract String getParameterString();

  public abstract boolean setParameterString(String paramString);

  public abstract int getMaxResponseLength();

  public abstract String getAllowedCharacters();

  public abstract String getPrompt();
}