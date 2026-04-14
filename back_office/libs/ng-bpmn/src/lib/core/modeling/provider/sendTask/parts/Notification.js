import {html} from 'htm/preact';
import {
  CheckboxEntry,
  isCheckboxEntryEdited,
  isSelectEntryEdited,
  isTextAreaEntryEdited,
  isTextFieldEntryEdited,
  SelectEntry,
  TextAreaEntry,
  TextFieldEntry
} from '@bpmn-io/properties-panel';
import {useService} from 'bpmn-js-properties-panel';
import {is} from "bpmn-js/lib/util/ModelUtil";

export default function NotificationProvider(users,groupList,roles,notifications) {
  return {
    getGroups(element) {
      return function(groups) {
        const {sendTask, notification} = element.businessObject;
        if (is(element, 'bpmn:SendTask') && notification) {
          groups.push(createNotificationGroup(element, users, groupList, roles,notifications));
        }

        return groups;
      };
    }
  };
}

function createNotificationGroup(element, users, groupList, roles,notifications) {
  return {
    id: 'notificationSection',
    label: 'Notification Section',
    entries: [
      {
        id: 'notificationList',
        element,
        component:(props) => NotificationList(props,notifications),
        isEdited: isSelectEntryEdited
      },
      {
        id: 'startCheck',
        element,
        component: StartCheck,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'stopCheck',
        element,
        component: StopCheck,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'variableNotif',
        element,
        component: VariableNotif,
        isEdited: isTextFieldEntryEdited
      }
    ]
  };
}

function UserCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.userCheck || false;

  const setValue = value => {
    modeling.updateProperties(element, { userCheck: value });
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('User')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function GroupCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.groupCheck || false;

  const setValue = value => {
    modeling.updateProperties(element, { groupCheck: value });
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Group')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
function RoleCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.roleCheck || false;

  const setValue = value => {
    modeling.updateProperties(element, { roleCheck: value});
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Role')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function SendTo(props, users, groupList, roles) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  let sendTo = typeof element.businessObject.sendTo === 'string'
    ? JSON.parse(element.businessObject.sendTo)
    : element.businessObject.sendTo || { user: [''], group: [''], role: [''] };

  const getValue = (type, index) => {
    return sendTo[type][index] || '';
  };

  const setValue = (type, index, value) => {
    sendTo[type][index] = value;
    modeling.updateProperties(element, { sendTo: JSON.stringify(sendTo) });
  };

  const addEntry = (type) => {
    sendTo[type].push('');
    modeling.updateProperties(element, { sendTo: JSON.stringify(sendTo) });
  };

  const removeEntry = (type, index) => {
    sendTo[type].splice(index, 1);
    modeling.updateProperties(element, { sendTo: JSON.stringify(sendTo) });
  };

  const getUsersOptions = () => {
    const userOptions = users.map(user => ({ label: `${user.firstName} ${user.lastName} (${user.username})`, value: user.id }));
    return userOptions;
  };

  const getGroupsOptions = () => {
    const groupOptions = groupList.map(group => ({ label: group.label, value: group.id }));
    return groupOptions;
  };

  const getRolesOptions = () => {
    const roleOptions = roles.map(role => ({ label: role.label, value: role.id }));
    return roleOptions;
  };

  return html`
    ${element.businessObject.userCheck
      ? html`
        ${sendTo.user.map((user, index) => html`
          <${SelectEntry}
            id=${id}-user-${index}
            element=${element}
            label=${translate('Users')}
            getValue=${() => getValue('user', index)}
            setValue=${(newValue) => setValue('user', index, newValue)}
            getOptions=${getUsersOptions}
            debounce=${debounce}
          />
          <button type="button" class="remove-camunda feather icon-trash-2" onClick=${() => removeEntry('user', index)}  title="Supprimer cet utilisateur"></button>
        `)}
        <button type="button" class="add-camunda feather icon-plus-circle" onClick=${() => addEntry('user')}  title="Ajouter un utilisateur"></button>
      `
      : null}

    ${element.businessObject.groupCheck
      ? html`
        ${sendTo.group.map((group, index) => html`
          <${SelectEntry}
            id=${id}-group-${index}
            element=${element}
            label=${translate('Groups')}
            getValue=${() => getValue('group', index)}
            setValue=${(newValue) => setValue('group', index, newValue)}
            getOptions=${getGroupsOptions}
            debounce=${debounce}
          />
          <button type="button" class="remove-camunda feather icon-trash-2" onClick=${() => removeEntry('group', index)} title="Supprimer ce groupe"></button>
        `)}
        <button type="button" class="add-camunda feather icon-plus-circle" onClick=${() => addEntry('group')} title="Ajouter un groupe"></button>
      `
      : null}

    ${element.businessObject.roleCheck
      ? html`
        ${sendTo.role.map((role, index) => html`
          <${SelectEntry}
            id=${id}-role-${index}
            element=${element}
            label=${translate('Roles')}
            getValue=${() => getValue('role', index)}
            setValue=${(newValue) => setValue('role', index, newValue)}
            getOptions=${getRolesOptions}
            debounce=${debounce}
          />
          <button type="button" class="remove-camunda feather icon-trash-2" onClick=${() => removeEntry('role', index)} title="Supprimer ce rôle"></button>
        `)}
        <button type="button" class="add-camunda feather icon-plus-circle"  onClick=${() => addEntry('role')} title="Ajouter un rôle"></button>
      `
      : null}
  `;
}






function NotifType(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');


  const setValue = (value) => {
    return modeling.updateProperties(element, {
      notifType: value
    });
  };

  const getValue = () => {
    return element.businessObject.notifType || '';
  };


  const getOptions = () => {
    return [
      { label: 'Mail', value: 'mail' },
      { label: 'Notification', value: 'notification' },
      { label: 'Mail + Notification', value: 'mail_notification' }

    ];
  };


  return html`
    <${SelectEntry}
      id=${id}
      element=${element}
      label=${translate('Notification by')}
      getValue=${getValue}
      setValue=${setValue}
      getOptions=${getOptions}
      debounce=${debounce}
    />

  `;
}
function MessageSubject(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.messageSubject;
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
      messageSubject: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Subject') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
  />`;
}
function MessageBody(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.messageBody;
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
      messageBody: value
    });
  };

  return html`<${TextAreaEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Message Body') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
  />`;
}
function NotificationList(props,notifications) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');


  const getValueNotifications = () => element.businessObject.notificationList || '';

  const setValueNotifications = (value) => modeling.updateProperties(element,
      {notificationList: value});
  const getNotificationsItem = () => {
    const TemplateOptions = notifications.map((notificationList) => ({
      label: notificationList.name, value: notificationList.id
    }));
    return TemplateOptions;
  };
  return html`
    <${SelectEntry}
      id=${id}
      element=${element}
      label=${translate('Notification List')}
      getValue=${getValueNotifications}
      setValue=${setValueNotifications}
      getOptions=${getNotificationsItem}
      debounce=${debounce}
    />

  `;
}
function StartCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.startCheck || false;

  const setValue = value => {
    modeling.updateProperties(element, { startCheck: value ,stopCheck: false});
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Start')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
function StopCheck (props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.stopCheck || false;

  const setValue = value => {
    modeling.updateProperties(element, { stopCheck: value, startCheck: false});
  };

  return html`<${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Stop')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />    
  `;
}
function VariableNotif(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.variableNotif;
  };

  const setValue = value => {
    return modeling.updateProperties(element, {
      variableNotif: value
    });
  };

  return html`<${TextFieldEntry}
    id=${ id }
    element=${ element }
    label=${ translate('Variable Notification') }
    getValue=${ getValue }
    setValue=${ setValue }
    debounce=${ debounce }
  />`;
}

