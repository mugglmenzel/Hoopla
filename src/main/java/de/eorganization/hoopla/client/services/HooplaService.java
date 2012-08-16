package de.eorganization.hoopla.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.eorganization.hoopla.shared.model.Member;
import de.eorganization.hoopla.shared.model.ahp.configuration.Decision;
import de.eorganization.hoopla.shared.model.ahp.configuration.DecisionTemplate;
import de.eorganization.hoopla.shared.model.ahp.values.Evaluation;
import de.eorganization.hoopla.shared.model.ahp.values.EvaluationResult;

/**
 * The client side stub for the RPC service.
 */

@RemoteServiceRelativePath("hoopla")
public interface HooplaService extends RemoteService {
	
	public Decision storeDecision(Decision newDecision);
	
	public DecisionTemplate storeDecisionTemplate(DecisionTemplate newTemplate);
	
	public List<Decision> getDecisions(String userId);
	
	public List<DecisionTemplate> getDecisionTemplates();
	
	public Decision getDecision(Long id);
	
	public Decision getDecision(Decision dec);
	
	public DecisionTemplate getDecisionTemplate(DecisionTemplate dec);
	
	public DecisionTemplate getDecisionTemplate(Long id);
	
	public Member registerMember(Member member);
	
	public Member updateMember(Member member);
	
	public EvaluationResult evaluate(Decision decision, List<Evaluation> eval, int precision) throws Exception;

}
