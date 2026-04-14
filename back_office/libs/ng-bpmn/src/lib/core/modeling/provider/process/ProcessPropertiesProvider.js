import VersionTag from './parts/VersionTag';
import Startable from './parts/Startable';
import { is } from 'bpmn-js/lib/util/ModelUtil';
import TimeToLive from './parts/TimeToLive';
import { ListGroup } from '@bpmn-io/properties-panel';
import ParametersProps from './parts/ParametersProps';
import ParametersExecution from './parts/ParametersExecution';
import FieldCandidatePropss from './parts/FieldCandidateProps';
import PriorityTask from './parts/PriorityTaskProps';
import jobPriority from './parts/JobPriorityProps';
import CategoryProvider from './parts/Category';



const LOW_PRIORITY = 500;

/**
 * A provider with a `#getGroups(element)` method
 * that exposes groups for a diagram element.
 *
 * @param {PropertiesPanel} propertiesPanel
 * @param {Function} translate
 */
export default function ProcessPropertiesProvider(propertiesPanel, translate,eventBus,injector) {

  // API ////////

  /**
   * Return the groups provided for the given element.
   *
   * @param {DiagramElement} element
   *
   * @return {(Object[]) => (Object[])} groups middleware
   */
  this.getGroups = function(element) {

    /**
     * We return a middleware that modifies
     * the existing groups.
     *
     * @param {Object[]} groups
     *
     * @return {Object[]} modified groups
     */
    return function(groups) {


      if (is(element, 'bpmn:Process') || is(element, 'bpmn:Participant')) {
        groups.push(createProcessGroup(element, translate));
        groups.push(createProcessTimeToLiveGroup(element, translate));
        groups.push(createCandidateGroup(element, translate));
        groups.push(createTaskPriorityGroup(element, translate));
        groups.push(createJobPriorityGroup(element, translate));
        groups.push(createExecutionGroup(element, injector, translate));
        groups.push(createParametersGroup(element, injector, translate));

   }
      if (is(element, 'bpmn:Process')) {
        groups.push(createCategoryGroup(element, translate));
      }

      if (is(element, 'bpmn:UserTask') || is(element, 'bpmn:StartEvent') || is(element, 'bpmn:ExclusiveGateway') || is(element, 'bpmn:ServiceTask') || is(element, 'bpmn:ComplexGateway') || is(element, 'bpmn:InclusiveGateway') || is(element, 'bpmn:EventBasedGateway') || is(element, 'bpmn:ParallelGateway')  || is(element, 'bpmn:ReceiveTask') || is(element, 'bpmn:EndEvent') || is(element, 'bpmn:SubProcess') || is(element ,'bpmn:BusinessRuleTask') || is(element, 'bpmn:SequenceFlow'))  {
        groups.push(createExecutionGroup(element, injector, translate));
        groups.push(createParametersGroup(element, injector, translate));

      }

      const {sendTask, notification} = element.businessObject;
      if ( is(element, 'bpmn:SendTask') && sendTask) {
        groups.push(createExecutionGroup(element, injector, translate));
        groups.push(createParametersGroup(element, injector, translate));
      }

      return groups;
    };
  };


  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

ProcessPropertiesProvider.$inject = ['propertiesPanel', 'translate','eventBus','injector'];


function createProcessGeneralGroup(element, translate) {

  const magicGroup = {
    id: 'versionTag',
    label: translate('Version Tag'),
    entries: VersionTag(element),
    tooltip: translate('Version Tag')
  };
  return magicGroup;
}
function createProcessGroup(element, translate) {

  const magicGroup = {
    id: 'startatble',
    label: translate('TaskList'),
    entries: Startable(element),
    tooltip: translate('TaskList')
  };
  return magicGroup;
}
function createProcessTimeToLiveGroup(element, translate) {

  const magicGroup = {
  id: 'timeToLive',
  label: translate('History Cleanup'),
  entries: TimeToLive(element),
  tooltip: translate('History Cleanup')
  }
;
  return magicGroup;
}
function createParametersGroup(element, injector, translate) {

  // Create a group called "parameters".
  const parametersGroup = {
    id: 'parameters',
    label: translate('Extension Properties'),
    component: ListGroup,
    ...ParametersProps({ element, injector })
  };

  return parametersGroup;
}
function createExecutionGroup(element, injector, translate) {


  const parametersGroup = {
    id: 'parameterExecutions',
    label: translate('Execution Listner'),
    component: ListGroup,
    ...ParametersExecution({ element, injector })
  };

  return parametersGroup;
}
function createCandidateGroup(element, translate) {

  // create a group called "Magic properties".
  const candidateGroup = {
    id: 'candidateStarterGroups',
    label: translate('Candidate starter '),
    entries: FieldCandidatePropss(element),
    tooltip: translate('Candidate starter ')
  };

  return candidateGroup;
}

function createTaskPriorityGroup(element, translate) {

  // create a group called "Magic properties".
  const taskGroup = {
    id: 'taskPriority',
    label: translate('External task'),
    entries: PriorityTask(element),
    tooltip: translate('External task')
  };

  return taskGroup;
}
function createJobPriorityGroup(element, translate) {

  // create a group called "Magic properties".
  const jobGroup = {
    id: 'jobPriority',
    label: translate('Job execution'),
    entries: jobPriority(element),
    tooltip: translate('Job execution')
  };

  return jobGroup;
}
function createCategoryGroup(element, translate) {


  const categoryGroup = {
    id: 'category',
    label: translate('Category'),
    entries: CategoryProvider(element),

  };

  return categoryGroup;
}
