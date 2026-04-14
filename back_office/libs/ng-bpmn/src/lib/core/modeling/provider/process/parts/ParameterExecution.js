import {
  SelectEntry,
  TextAreaEntry,
  TextFieldEntry
} from '@bpmn-io/properties-panel';

import {useService} from 'bpmn-js-properties-panel';
import FieldInjection from './FieldInjection';
import {html} from 'htm/preact';

export default function ParameterExecution(props) {

  const {
    idPrefix,
    parameter
  } = props;

  const entries = [
    {
      id: idPrefix + '-event',
      component: EventType,
      idPrefix,
      parameter
    },
    {
        id: idPrefix + '-listnerType',
        component: ListnerType,
        idPrefix,
        parameter
      },
    // {
    //   id: idPrefix + '-format',
    //   component: Format,
    //   idPrefix,
    //   parameter
    // },
    {
      id: idPrefix + '-extensions',
      component: FieldInjection,
      idPrefix,
      parameter
    }
  ];

  return entries;
}

function EventType(props) {
  const {
    idPrefix,
    element,
    parameter
  } = props;

  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const setValue = (value) => {
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: parameter,
      properties: {
        event: value
      }
    });
  };

  const getValue = (parameter) => {
    return parameter.event;
  };

  const getOptions = () => {
    return [
      { label: 'Start', value: 'start' },
      { label: 'End', value: 'end' },
    ];
  };

  return html`<${SelectEntry}
    id=${idPrefix + '-event'}
    element=${element}
    label=${translate('Event Type')}
    getValue=${getValue}
    setValue=${setValue}
    getOptions=${getOptions}
    debounce=${debounce}
  />`;
}
function ListnerType(props) {
    const {
      idPrefix,
      element,
      parameter
    } = props;

    const commandStack = useService('commandStack');
    const translate = useService('translate');
    const debounce = useService('debounceInput');

    const setValue = (value) => {
      parameter.listnerType = value;
      commandStack.execute('element.updateModdleProperties', {
        element,
        moddleElement: parameter,
        properties: {
          listnerType: value
        }
      });
    };

    const getValue = () => {
      return parameter.listnerType;
    };

    const getOptions = () => {
      return [
        { label: 'Java Class', value: 'Java Class' },
        { label: 'Expression', value: 'Expression' },
        { label: 'Delegate Expression', value: 'Delegate Expression' },
        { label: 'Script', value: 'script' },
      ];
    };
    const getOptionsType = () => {
        return [
          { label: 'External Resource', value: 'External Resource' },
          { label: 'Inline Script', value: 'Inline Script' },
        ];
      };
    const showScriptFields = parameter.listnerType === 'script';
    const showJavaClassFields = parameter.listnerType === 'Java Class';
    const showExpressionFields = parameter.listnerType === 'Expression';
    const showDelegateExpressionFields = parameter.listnerType === 'Delegate Expression';
    const showTypeScriptFields = parameter.type === 'Inline Script';
    const showTypeExternalRessourcetFields = parameter.type === 'External Resource';

    return html`
      <${SelectEntry}
        id=${idPrefix + '-listnerType'}
        element=${element}
        label=${translate('Listner Type')}
        getValue=${getValue}
        setValue=${setValue}
        getOptions=${getOptions}
        debounce=${debounce}
      />
      ${showScriptFields && html`
        <${TextFieldEntry}
          element=${parameter}
          id=${idPrefix + '-scriptFormat'}
          label=${translate('Format')}
          getValue=${() => parameter.scriptFormat}
          setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
            element,
            moddleElement: parameter,
            properties: { scriptFormat: value }
          })}
          debounce=${debounce}
        />
        <${SelectEntry}
          element=${parameter}
          id=${idPrefix + '-type'}
          label=${translate('Type')}
          getValue=${() => parameter.type || undefined}
          setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
            element,
            moddleElement: parameter,
            properties: { type: value }
          })}
          getOptions=${getOptionsType}
          debounce=${debounce}
        />
      `}
      ${showTypeScriptFields && html`
      <${TextAreaEntry}
        element=${parameter}
        id=${idPrefix + '-script'}
        label=${translate('Script')}
        getValue=${() => parameter.script}
        setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
          element,
          moddleElement: parameter,
          properties: { script: value }
        })}
        debounce=${debounce}
      />


    `}
    ${showTypeExternalRessourcetFields && html`
    <${TextFieldEntry}
      element=${parameter}
      id=${idPrefix + '-resource'}
      label=${translate('Resource')}
      getValue=${() => parameter.resource}
      setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
        element,
        moddleElement: parameter,
        properties: { resource: value }
      })}
      debounce=${debounce}
    />


  `}
  ${showJavaClassFields && html`
  <${TextFieldEntry}
    element=${parameter}
    id=${idPrefix + '-class'}
    label=${translate('Java Class')}
    getValue=${() => parameter.class}
    setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: parameter,
      properties: { class: value }
    })}
    debounce=${debounce}
  />


`}
${showExpressionFields && html`
<${TextFieldEntry}
  element=${parameter}
  id=${idPrefix + '-expression'}
  label=${translate('Expression')}
  getValue=${() => parameter.expression}
  setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
    element,
    moddleElement: parameter,
    properties: { expression: value }
  })}
  debounce=${debounce}
/>


`}
${showDelegateExpressionFields && html`
<${TextFieldEntry}
  element=${parameter}
  id=${idPrefix + '-delegateExpression'}
  label=${translate('Delegate Expression')}
  getValue=${() => parameter.delegateExpression}
  setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
    element,
    moddleElement: parameter,
    properties: { delegateExpression: value }
  })}
  debounce=${debounce}
/>


`}
    `;
  }
