/**
 * 
 */
package de.eorganization.hoopla.shared.model.ahp.configuration.attribute;


/**
 * @author mugglmenzel
 *
 */
public enum ECloudDecisionAttribute implements IEAttribute {

		COSTPERHOUR("costperhour"), PERFORMANCE("performance");
		
		/**
		 * @uml.property  name="name"
		 */
		private String name;
		
		private ECloudDecisionAttribute(String name) {
			this.setName(name);
		}

		/**
		 * @param name  the name to set
		 * @uml.property  name="name"
		 */
		@Override
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return  the name
		 * @uml.property  name="name"
		 */
		@Override
		public String getName() {
			return name;
		}
	
}
