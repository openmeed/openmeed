package me.ebenezergraham.honours.platform.interfaces;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.model.Reward;

import java.util.Map;

public interface IRewardEngine {

  void process(Payload payload);

  void validationCriteria(Map<String,Boolean> criteria);

  boolean validate(Payload payload, Reward reward);
}
