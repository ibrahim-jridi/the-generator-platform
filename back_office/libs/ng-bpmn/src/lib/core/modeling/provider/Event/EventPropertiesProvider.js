import { is } from 'bpmn-js/lib/util/ModelUtil';

import Initiator from './parts/Initiator';
import Async from './parts/Async';
import JobExecution from './parts/JobExecution';
import Forms from './parts/Forms';
import UserAssignmentProvider from "../userTask/parts/UserAssignment";
import FormsProvider from "./parts/Forms";

const LOW_PRIORITY = 500;

export default function EventPropertiesProvider(propertiesPanel, translate, eventBus, injector) {
  this.getGroups = function(element) {
    return function(groups) {
      if (is(element, 'bpmn:StartEvent')) {

        groups.push(createstartInitiatorGroup(element, translate));

        const asyncGroup = createstAsynchGroup(element, translate);
        groups.push(asyncGroup);

        const { asyncBefore, asyncAfter } = element.businessObject;
        if (asyncBefore || asyncAfter) {
          groups.push(createstJobExecutionGroup(element, translate));
        }


      }
      if (is(element, 'bpmn:UserTask') || is(element, 'bpmn:ExclusiveGateway') || is(element, 'bpmn:ServiceTask') || is(element, 'bpmn:ComplexGateway') || is(element, 'bpmn:InclusiveGateway') || is(element, 'bpmn:EventBasedGateway') || is(element, 'bpmn:ParallelGateway')  || is(element, 'bpmn:ReceiveTask') || is(element, 'bpmn:EndEvent') || is(element, 'bpmn:SubProcess') || is(element ,'bpmn:BusinessRuleTask')) {
        const asyncGroup = createstAsynchGroup(element, translate);
        groups.push(asyncGroup);

        const { asyncBefore, asyncAfter } = element.businessObject;
        if (asyncBefore || asyncAfter) {
          groups.push(createstJobExecutionGroup(element, translate));
     }
      }
      const {sendTask, notification} = element.businessObject;
      if ( is(element, 'bpmn:SendTask') && sendTask) {
        const asyncGroup = createstAsynchGroup(element, translate);
        groups.push(asyncGroup);

        const { asyncBefore, asyncAfter } = element.businessObject;
        if (asyncBefore || asyncAfter) {
          groups.push(createstJobExecutionGroup(element, translate));
      }
        }
      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

EventPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

function createstartInitiatorGroup(element, translate) {
  const startInitiatorGroup = {
    id: 'startInitiator',
    label: translate('Start initiator'),
    entries: Initiator(element),

  };
  return startInitiatorGroup;
}
function createstAsynchGroup(element, translate, groups) {
  const asyncGroup = {
    id: 'async',
    label: translate('Asynchronous continuations'),
    entries: Async(element),
  };



  return asyncGroup;
}

  function createstJobExecutionGroup(element, translate) {
    const startInitiatorGroup = {
      id: 'jobExecution',
      label: translate('Job Execution'),
      entries: JobExecution(element),

    };
    return startInitiatorGroup;
  }
