import {html} from 'htm/preact';
import {
  CheckboxEntry,
  isCheckboxEntryEdited,
  isSelectEntryEdited,
  isTextFieldEntryEdited,
  SelectEntry,
  TextFieldEntry
} from '@bpmn-io/properties-panel';
import {useService} from 'bpmn-js-properties-panel';
import {is} from 'bpmn-js/lib/util/ModelUtil';

export default function UserAssignmentProvider(users, groupList, roles) {
  return {
    getGroups(element) {
      return function (groups) {
        if (is(element, 'bpmn:UserTask')) {
          groups.push(createUserAssignmentGroup(element, users, groupList, roles));
        }
        // Add other conditions for different elements if needed
        return groups;
      };
    }
  };
}

function createUserAssignmentGroup(element, users, groupList, roles) {
  return {
    id: 'userAssignment',
    label: 'User Assignment',
    entries: [
      {
        id: 'user',
        element,
        component: User,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'variable',
        element,
        component: Variable,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'groupToCheck',
        element,
        component: GroupToCheck,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'roleToCheck',
        element,
        component: RoleToCheck,
        isEdited: isCheckboxEntryEdited
      },
      {
        id: 'assignee',
        element,
        component: (props) => Assignee(props, users, groupList, roles),
        isEdited: isSelectEntryEdited
      },
      {
        id: 'candidateGroups',
        element,
        component: CandidateGroups,
        isEdited: isTextFieldEntryEdited
      },
      {
        id: 'candidateUsers',
        element,
        component: CandidateUsers,
        isEdited: isTextFieldEntryEdited
      },
      {
        id: 'dueDate',
        element,
        component: DueDate,
        isEdited: isTextFieldEntryEdited
      },
      {
        id: 'followUpDate',
        element,
        component: FollowUpDate,
        isEdited: isTextFieldEntryEdited
      },
      {
        id: 'priority',
        element,
        component: Priority,
        isEdited: isTextFieldEntryEdited
      }
    ]
  };
}

function User(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.user || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { user: value, variable: false, roleToCheck: false, groupToCheck: false }); // Ensure Variable is unchecked
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Exist User')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function Variable(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.variable || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { variable: value, user: false, roleToCheck: false, groupToCheck: false }); // Ensure User is unchecked
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Variable')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function Assignee(props, users, groupList, roles) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.assignee || '';
  };

  const setValue = (value) => {
    modeling.updateProperties(element, { assignee: value });
  };

  const getOptions = () => {
    const userOptions = users.map((user) => ({
      label: user.firstName + ' ' + user.lastName + ' (' + user.username + ')',
      value: user.id
    }));
    return userOptions;
  };
  const getGroupsOptions = () => {
    const groupOptions = groupList.map((group) => ({ label: group.label, value: group.id }));
    return groupOptions;
  };
  const getRolesOptions = () => {
    const roleOptions = roles.map((role) => ({ label: role.label, value: role.id }));
    return roleOptions;
  };

  return element.businessObject.user
    ? html` <${SelectEntry}
        id=${id}
        element=${element}
        label=${translate('Assignee')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getOptions}
        debounce=${debounce}
      />`
    : element.businessObject.variable
    ? html` <${TextFieldEntry}
        id=${id}
        element=${element}
        label=${translate('Assignee')}
        getValue=${getValue}
        setValue=${setValue}
        debounce=${debounce}
      />`
    : element.businessObject.groupToCheck
    ? html` <${SelectEntry}
        id=${id}
        element=${element}
        label=${translate('Group')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getGroupsOptions}
        debounce=${debounce}
      />`
    : element.businessObject.roleToCheck
    ? html` <${SelectEntry}
        id=${id}
        element=${element}
        label=${translate('Role')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getRolesOptions}
        debounce=${debounce}
      />`
    : null;
}

function GroupToCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.groupToCheck || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { groupToCheck: value, variable: false, user: false, roleToCheck: false }); // Ensure Variable is unchecked
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Group')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function RoleToCheck(props) {
  const { element, id } = props;
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => element.businessObject.roleToCheck || false;

  const setValue = (value) => {
    modeling.updateProperties(element, { roleToCheck: value, variable: false, user: false, groupToCheck: false }); // Ensure Variable is unchecked
  };

  return html` <${CheckboxEntry}
    id=${id}
    element=${element}
    label=${translate('Role')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function CandidateUsers(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.candidateUsers || '';
  };

  const setValue = (value) => {
    return modeling.updateProperties(element, {
      candidateUsers: value
    });
  };

  return html` <${TextFieldEntry}
    id=${id}
    element=${element}
    label=${translate('Candidate Users')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
    tooltip=${translate('Candidate Users')}
  />`;
}

function CandidateGroups(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.candidateGroups || '';
  };

  const setValue = (value) => {
    return modeling.updateProperties(element, {
      candidateGroups: value
    });
  };

  return html` <${TextFieldEntry}
    id=${id}
    element=${element}
    label=${translate('Candidate Groups')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
    tooltip=${translate('Candidate Groups')}
  />`;
}

function DueDate(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.dueDate || '';
  };

  const setValue = (value) => {
    return modeling.updateProperties(element, {
      dueDate: value
    });
  };

  return html` <${TextFieldEntry}
    id=${id}
    element=${element}
    label=${translate('Due Date')}
    description=${translate('The due date as en EL expression (e.g. ${someDate}) or an ISO date (e.g. 2015-06-26-T09:54:00).')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function FollowUpDate(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.followUpDate || '';
  };

  const setValue = (value) => {
    return modeling.updateProperties(element, {
      followUpDate: value
    });
  };

  return html` <${TextFieldEntry}
    id=${id}
    element=${element}
    label=${translate('Follow up date')}
    description=${translate('The follow up date as en EL expression (e.g. ${someDate}) or an ISO date (e.g. 2015-06-26-T09:54:00).')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}

function Priority(props) {
  const { element, id } = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    return element.businessObject.priority || '';
  };

  const setValue = (value) => {
    return modeling.updateProperties(element, {
      priority: value
    });
  };

  return html` <${TextFieldEntry}
    id=${id}
    element=${element}
    label=${translate('Priority')}
    getValue=${getValue}
    setValue=${setValue}
    debounce=${debounce}
  />`;
}
