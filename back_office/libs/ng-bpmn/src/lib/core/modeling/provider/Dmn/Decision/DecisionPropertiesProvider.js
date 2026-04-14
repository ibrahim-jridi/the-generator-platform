import { is } from 'bpmn-js/lib/util/ModelUtil';
import DecisionProps from './parts/DecisionProps';

const LOW_PRIORITY = 500;

export default function DecisionPropertiesProvider(propertiesPanel, translate, eventBus, injector) {
  this.getGroups = function(element) {
    return function(groups) {



      if (is(element, 'dmn:Decision')) {
        groups.push(CreateDecisionGroup(element, translate));
      }
      return groups;
    };
  }

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

DecisionPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

function CreateDecisionGroup(element, translate) {

    const DecisionGroup = {
      id: 'historyTimeToLive',
      label: translate('History Cleanup'),
      entries: DecisionProps(element),
      tooltip: translate('History Cleanup')
    };
  
    return DecisionGroup;
  }
  