package de.fzi.aotearoa.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fzi.aotearoa.shared.model.ahp.configuration.Decision;
import de.fzi.aotearoa.shared.model.ahp.configuration.DecisionTemplate;
import de.fzi.aotearoa.shared.model.ahp.values.Evaluation;
import de.fzi.aotearoa.shared.model.ahp.values.EvaluationResult;

/**
 * The client side stub for the RPC service.
 */

@RemoteServiceRelativePath("aotearoa")
public interface AotearoaService extends RemoteService {
	
	public Decision storeDecision(Decision newDecision);
	
	public DecisionTemplate storeDecisionTemplate(DecisionTemplate newTemplate);
	
	public List<Decision> getDecisions();
	
	public List<DecisionTemplate> getDecisionTemplates();
	
	public Decision getDecision(Long id);
	
	public Decision getDecision(Decision dec);
	
	public DecisionTemplate getDecisionTemplate(DecisionTemplate dec);
	
	public DecisionTemplate getDecisionTemplate(Long id);
	
	public EvaluationResult evaluate(Decision decision, List<Evaluation> eval, int precision) throws Exception;

}
