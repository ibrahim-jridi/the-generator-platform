import { html } from 'htm/preact';
import {CheckboxEntry, isCheckboxEntryEdited, isSelectEntryEdited} from '@bpmn-io/properties-panel';
import { useService } from 'bpmn-js-properties-panel';

export default function NotifOrSendTask(element) {
  const sendTask = element.businessObject.sendTask || false;
  const notification = element.businessObject.notification || false;

  return [
    {
      id: 'sendTask',
      element,
      component: SendTask,
      isEdited: isSelectEntryEdited
    },
    {
      id: 'notification',
      element,
      component: Notification,
      isEdited: isSelectEntryEdited
    }
  ];
}

function SendTask(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.sendTask || false;

  const setValue = value => {
    modeling.updateProperties(element, { sendTask: value, notification: false });
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Send Task')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
function Notification(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.notification || false;

  const setValue = value => {
    modeling.updateProperties(element, { notification: value, sendTask: false });
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Notification')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
