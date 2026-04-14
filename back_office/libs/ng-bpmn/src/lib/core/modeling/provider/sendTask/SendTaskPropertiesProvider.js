
import NotifOrSendTask from "./parts/NotifOrSendTask";
import {is} from "bpmn-js/lib/util/ModelUtil";


const LOW_PRIORITY = 500;

export default function SendTaskPropertiesProvider(propertiesPanel, translate, eventBus, injector) {

  this.getGroups = function(element) {
    return function(groups) {
      if (is(element, 'bpmn:SendTask')) {
        const asyncGroup = notifOrSendTaskGroup(element, translate);

        const documentationGroupIndex = groups.findIndex(group => group.id === 'documentation');

        if (documentationGroupIndex !== -1) {
          groups.splice(documentationGroupIndex + 1, 0, asyncGroup);
        } else {
          groups.push(asyncGroup);
        }
      }

      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

SendTaskPropertiesProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];

function notifOrSendTaskGroup(element, translate, groups) {
  const notifOrSendTaskGroup = {
    id: 'notifOrSendTask',
    label: translate('Notification Or Send Task'),
    entries: NotifOrSendTask(element),
  };



  return notifOrSendTaskGroup;
}

