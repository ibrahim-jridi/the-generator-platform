import {
  SelectEntry,
  TextAreaEntry,
  TextFieldEntry
} from '@bpmn-io/properties-panel';

import {useService} from 'bpmn-js-properties-panel';

import {html} from 'htm/preact';

export default function OutputProps(props) {

  const {
    idPrefix,
    parameter
  } = props;

  const entries = [
    {
      id: idPrefix + '-name',
      component: Name,
      idPrefix,
      parameter
    },
    {
      id: idPrefix + '-assignmentType',
      component: AssignmentType,
      idPrefix,
      parameter
    },

  ];

  return entries;
}

function Name(props) {
  const {
    idPrefix,
    element,
    parameter
  } = props;

  const commandStack = useService('commandStack');
  const translate = useService('translate');
  const debounce = useService('debounceInput');

  const setValue = (value) => {
    parameter.assignmentType = value;
    commandStack.execute('element.updateModdleProperties', {
      element,
      moddleElement: parameter,
      properties: {
        name: value
      }
    });
  };

  const getValue = (parameter) => {
    return parameter.name;
  };

  return TextFieldEntry({
    element: parameter,
    id: idPrefix + '-name',
    label: translate('Local Variable Name'),
    getValue,
    setValue,
    debounce
  });
}

function AssignmentType(props) {
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
        assignmentType: value
      }
    });
  };

  const getValue = (parameter) => {
    return parameter.assignmentType;
  };

  const getOptions = () => {
    return [
      { label: 'Script', value: 'script' },
      { label: 'String or Expression', value: 'stringExpression' },

    ];
  };
  const getOptionsType = () => {
      return [
        { label: 'External Ressource', value: 'externalRessource' },
        { label: 'Inline Script', value: 'inlineScript' },
      ];
    };
  const showScriptFields = parameter.assignmentType === 'script';
  const showExpressionFields = parameter.assignmentType === 'stringExpression';
  const showTypeScriptFields = parameter.type === 'inlineScript';
  const showTypeExternalRessourcetFields = parameter.type === 'externalRessource';

  return html`
    <${SelectEntry}
      id=${idPrefix + '-assignmentType'}
      element=${element}
      label=${translate('Assignment Type')}
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

${showExpressionFields && html`
<${TextFieldEntry}
element=${parameter}
id=${idPrefix + '-expression'}
description=${ translate('Start typing "${}" to create an expression') }
label=${translate('Value')}
getValue=${() => parameter.expression}
setValue=${(value) => commandStack.execute('element.updateModdleProperties', {
  element,
  moddleElement: parameter,
  properties: { expression: value }
})}
debounce=${debounce}
/>


`}

  `;
}
