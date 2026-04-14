import {
  isSelectEntryEdited,
  SelectEntry,
  TextFieldEntry
} from "@bpmn-io/properties-panel";
import {useService} from "bpmn-js-properties-panel";
import {html} from 'htm/preact';
import {is} from 'bpmn-js/lib/util/ModelUtil';

export default function TemplatesProvider(templates, decisionTables) {
  return {
    getGroups(element) {
      return function (groups) {
        if (is(element, 'bpmn:BusinessRuleTask') || is(element,
            'bpmn:ServiceTask') || is(element, 'bpmn:SendTask')) {
          groups.push(createImplementation(element, templates, decisionTables));
        }
        return groups;
      };
    }
  };

}

function createImplementation(element, templates, decisionTables) {

  return {
    id: 'type',
    label: 'Implementation',
    entries: [
      {
        id: 'type',
        element,
        component: (props) => ImplementationGroup(props, templates,
            decisionTables),
        isEdited: isSelectEntryEdited
      }
    ]
  };
}

function ImplementationGroup(props, templates, decisionTables) {
  const {element, id} = props;
  const {sendTask, notification} = element.businessObject;
  const notifType = element.businessObject.notifType
  const startCheck=element.businessObject.startCheck
  const stopCheck=element.businessObject.stopCheck
  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const getValue = () => {
    if (notification) {
      return 'class';
    }
    return element.businessObject[id];
  };
  const setValue = (value) => {
    modeling.updateProperties(element, {[id]: value});
  };

  const getValueTopic = () => {
    return element.businessObject.topic || '';
  };
  const setValueTopic = value => {
    return modeling.updateProperties(element, {
      topic: value
    });
  };

  const getValueReportDelegateServiceClass = () => {
    return element.businessObject.class = reportDelegateServiceClass;
  }
  const getValueCreateUserPPDelegateServiceClass = () => {
    return element.businessObject.class = createUserPPDelegateServiceClass;
  }
  const getValueCreateUserPMDelegateServiceClass = () => {
    return element.businessObject.class = createUserPMDelegateServiceClass;
  }
  const getValueSaveDesignationServiceClass = () =>{
    return element.businessObject.class = saveDesignationServiceClass;

  }
  const getValueCompleteProfileDelegateServiceClass = () => {
    return element.businessObject.class = completeProfileDelegateServiceClass;
  }
  const getValueRegistrationWaitingListDelegateServiceClass = () => {
    return element.businessObject.class = registrationWaitingListDelegateServiceClass;
  }
  const getValueFixVariablesPPForDMNDelegateServiceClassDelegateServiceClass = () => {
    return element.businessObject.class = fixVariablesPPForDMNDelegateServiceClass;
  }
  const getValueUnsubscribeFromWaitingListServiceClass= () => {
    return element.businessObject.class = unsubscribeFromWaitingListServiceClass;
  }
  const getValueRenewalWaitingListGovernorateDelegateServiceClass = () => {
    return element.businessObject.class = renewalWaitingListGovernorateDelegateServiceClass;
  }
  const getValueRenewalWaitingListCategoryDelegateServiceClass = () => {
    return element.businessObject.class = renewalWaitingListCategoryDelegateServiceClass;
  }
  const getValueSavePMDataDelegateServiceClass = () => {
    return element.businessObject.class = savePMDataDelegateServiceClass;
  }
  const getValueSaveAttributesDelegateServiceClass = () => {
    return element.businessObject.class = saveAttributesDelegateServiceClass;
  }
  const getValueCreatePMLabDelegateServiceClass = () => {
    return element.businessObject.class = createPMLabDelegateServiceClass;
  }
  const getValueClass = () => {
    if(startCheck!=null || stopCheck !=null && notification ){
        return element.businessObject.class = ''
    }
    return element.businessObject.class || '';
  };
  const isDisabled = () => {
      return element.businessObject.startCheck !=null || element.businessObject.stopCheck !=null
  };
  const setValueClass = value => {
    return modeling.updateProperties(element, {
      class: value
    });
  };

  const getValueDelegate = () => {
    return element.businessObject.delegateExpression || '';
  };
  const setValueDelegate = value => {
    return modeling.updateProperties(element, {
      delegateExpression: value
    });
  };

  const getValueExpression = () => {
    return element.businessObject.expression || '';
  };
  const setValueExpression = value => {
    return modeling.updateProperties(element, {
      expression: value
    });
  };

  const getValueResult = () => {
    return element.businessObject.resultVariable || '';
  };
  const setValueResult = value => {
    return modeling.updateProperties(element, {
      resultVariable: value
    });
  };

  const getValueConnector = () => {
    return element.businessObject.connectorId || '';
  };
  const setValueConnector = value => {
    return modeling.updateProperties(element, {
      connectorId: value
    });
  };

  const getValueDecision = () => {
    return element.businessObject.decisionRef || '';
  };
  const setValueDecision = value => {
    return modeling.updateProperties(element, {
      decisionRef: value
    });
  };

  const getDecisionItem = () => {
    const TemplateOptions = decisionTables.map((decisionTable) => ({
      label: `${decisionTable.name} : ${decisionTable.key} (${decisionTable.version})`,
      value: decisionTable.key
    }));
    return TemplateOptions;
  };

  const getValueBinding = () => {
    return element.businessObject.decisionRefBinding || '';
  };
  const setValueBinding = value => {
    return modeling.updateProperties(element, {
      decisionRefBinding: value
    });
  };

  const getValueTenant = () => {
    return element.businessObject.decisionRefTenantId || '';
  };
  const setValueTenant = value => {
    return modeling.updateProperties(element, {
      decisionRefTenantId: value
    });
  };

  const getValueVersion = () => {
    return element.businessObject.decisionRefVersion || '';
  };
  const setValueVersion = value => {
    return modeling.updateProperties(element, {
      decisionRefVersion: value
    });
  };

  const getValueVersionTag = () => {
    return element.businessObject.decisionRefVersionTag || '';
  };
  const setValueVersionTag = value => {
    return modeling.updateProperties(element, {
      decisionRefVersionTag: value
    });
  };

  const getValueMapDecision = () => {
    return element.businessObject.extensionElement || '';
  };
  const setValueMapDecision = value => {
    return modeling.updateProperties(element, {
      extensionElement: value
    });

  };

  const getValueTemplates = () => element.businessObject.template || '';

  const setValueTemplates = (value) => modeling.updateProperties(element,
      {template: value});

  const getTemplatesItem = () => {
    const TemplateOptions = templates.map((template) => ({
      label: template.name, value: template.id
    }));
    return TemplateOptions;
  };

  const getOptionsMapDecision = () => {
    return [
      {label: 'collectEntries(List<Object>)', value: 'collectEnteries'},
      {label: 'resultList(List<Map<String,Object>>)', value: ''},
      {label: 'singleEntery(TypedValued)', value: 'singleEntery'},
      {label: 'singleResult(Map<Sting,Object>)', value: 'singleResult'},];
  }

  const getOptionsBinding = () => {
    return [
      {label: 'Deployment', value: 'deployment'},
      {label: 'Latest', value: ''},
      {label: 'Version', value: 'version'},
      {label: 'VersionTag', value: 'versionTag'},];
  }

  const getOptions = () => {
    return [
      {label: 'DMN', value: 'dmn'},
      {label: 'External', value: 'external'},
      {label: 'Java class', value: 'class'},
      {label: 'Expression', value: 'expression'},
      {label: 'Delegate Expression', value: 'delegateExpression'},
      {label: 'Connector', value: 'connector'},
      {label: 'Report', value: 'report'},
      {label: 'Create PP', value: 'createUserPP'},
      {label: 'Create PM', value: 'createUserPM'},
      {label: 'Complete Profile', value: 'completeProfile'},
      {label: 'Registration for the waiting list', value: 'registrationWaitingList'},
      {label: 'Fix Variables PP For DMN', value: 'fixVariablesPPForDMN'},
      {label: 'Save Designation', value: 'saveDesignation'},
      {label: 'Unsubscribe from  Waiting List ', value: 'unsubscribeFromWaitingList'},
      {label: 'Renewal region change for the waiting list', value: 'renewalRegionWaitingList'},
      {label: 'Renewal category change for the waiting list', value: 'renewalCategoryWaitingList'},
      {label: 'Save PM Data', value: 'savePMData'},
      {label: 'Save Attributes ', value: 'saveAttributes'},
      {label: 'Create PM Lab', value: 'createPMLab'},
    ];
  }

  const renderTextField2 = () => {
    const selectedValue = getValueBinding();

    switch (selectedValue) {
      case 'version':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Version')}
              getValue=${getValueVersion}
              setValue=${setValueVersion}
              debounce=${debounce}
          />
        `;

      case 'versionTag':

        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('VersionTag')}
              getValue=${getValueVersionTag}
              setValue=${setValueVersionTag}
              debounce=${debounce}
          />
        `;

      default:
        ''

    }

  };

  const renderClass = () => {

    return html`
      <${TextFieldEntry}
          id=${id}
          element=${element}
          label=${translate('Java class')}
          getValue=${getValueReportDelegateServiceClass}
          setValue=${setValueClass}
          debounce=${debounce}
          disabled=${true}
      />
    `;
  }

  const renderTextField = () => {
    const selectedValue = getValue();

    switch (selectedValue) {

      case 'dmn':
        return html`
          <${SelectEntry}
              id=${id}
              element=${element}
              label=${translate('Decision reference')}
              getValue=${getValueDecision}
              setValue=${setValueDecision}
              getOptions=${getDecisionItem}
              debounce=${debounce}
          />
          <${SelectEntry}
              id=${id}
              element=${element}
              label=${translate('Binding')}
              getValue=${getValueBinding}
              setValue=${setValueBinding}
              getOptions=${getOptionsBinding}
              debounce=${debounce}
          />
          ${renderTextField2()}
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Tenant ID')}
              getValue=${getValueTenant}
              setValue=${setValueTenant}
              debounce=${debounce}
          />
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Result Variable')}
              getValue=${getValueResult}
              setValue=${setValueResult}
              debounce=${debounce}
          />
          ${getValueResult() ? html`
            <${SelectEntry}
                id=${id}
                element=${element}
                label=${translate('Map decision result')}
                getValue=${getValueMapDecision}
                setValue=${setValueMapDecision}
                getOptions=${getOptionsMapDecision}
                debounce=${debounce}
            />   ` : ''}


        `;

      case 'external':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Topic')}
              getValue=${getValueTopic}
              setValue=${setValueTopic}
              debounce=${debounce}
          />
        `;
      case 'class':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Java class')}
              getValue=${getValueClass}
              setValue=${setValueClass}
              debounce=${debounce}
              disabled=${isDisabled()}
          />
        `;
      case 'expression':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Expression')}
              getValue=${getValueExpression}
              setValue=${setValueExpression}
              debounce=${debounce}
          />
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Result Variable')}
              getValue=${getValueResult}
              setValue=${setValueResult}
              debounce=${debounce}
          />
        `;
      case 'delegateExpression':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Delegate expression')}
              getValue=${getValueDelegate}
              setValue=${setValueDelegate}
              debounce=${debounce}
          />
        `;
      case 'connector':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Connector ID')}
              getValue=${getValueConnector}
              setValue=${setValueConnector}
              debounce=${debounce}
          />
        `;
      case 'report':

        return html`
          <${SelectEntry}
              id=${id}
              element=${element}
              label=${translate('Template')}
              getValue=${getValueTemplates}
              setValue=${setValueTemplates}
              getOptions=${getTemplatesItem}
              debounce=${debounce}
          />
          ${renderClass()}
        `;
      case 'createUserPP':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueCreateUserPPDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'createUserPM':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueCreateUserPMDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'completeProfile':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueCompleteProfileDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'registrationWaitingList':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueRegistrationWaitingListDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
        case 'renewalRegionWaitingList':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueRenewalWaitingListGovernorateDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'fixVariablesPPForDMN':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueFixVariablesPPForDMNDelegateServiceClassDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'saveDesignation':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueSaveDesignationServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'unsubscribeFromWaitingList':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueUnsubscribeFromWaitingListServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'renewalCategoryWaitingList':
        return html`
          <${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Java class')}
            getValue=${getValueRenewalWaitingListCategoryDelegateServiceClass}
            setValue=${setValueClass}
            debounce=${debounce}
            disabled=${true}
          />
        `;
      case 'savePMData':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Java class')}
              getValue=${getValueSavePMDataDelegateServiceClass}
              setValue=${setValueClass}
              debounce=${debounce}
              disabled=${true}
          />
        `;
      case 'saveAttributes':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Java class')}
              getValue=${getValueSaveAttributesDelegateServiceClass}
              setValue=${setValueClass}
              debounce=${debounce}
              disabled=${true}
          />
        `;
      case 'createPMLab':
        return html`
          <${TextFieldEntry}
              id=${id}
              element=${element}
              label=${translate('Java class')}
              getValue=${getValueCreatePMLabDelegateServiceClass}
              setValue=${setValueClass}
              debounce=${debounce}
              disabled=${true}
          />
        `;
      default:
        return null;
    }
  };

  return html`
    <div>
      <${SelectEntry}
          id=${id}
          element=${element}
          label=${translate('Type')}
          getValue=${getValue}
          setValue=${setValue}
          getOptions=${getOptions}
          debounce=${debounce}
      />
      ${renderTextField()}
    </div>
  `;
}
