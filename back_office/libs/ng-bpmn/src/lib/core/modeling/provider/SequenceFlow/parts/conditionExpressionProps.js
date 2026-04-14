import {
    isSelectEntryEdited,
    SelectEntry,
    TextAreaEntry,
    TextFieldEntry
} from "@bpmn-io/properties-panel";
import {useService} from "bpmn-js-properties-panel";
import {html} from 'htm/preact';

export default function (element) {
    return [
        {
            id: 'type',
            element,
            component: ConditionGroupe,
            isEdited: isSelectEntryEdited,
        }
    ];
}

function ConditionGroupe(props) {
    // Retrieve necessary services
    const { element, id } = props;
    const modeling = useService('modeling');
    const translate = useService('translate');
    const debounce = useService('debounceInput');

    // Function to get the value of a property from the business object
    const getValue = () => {
        return element.businessObject[id]; };

    // Function to set the value of a property in the business object
    const setValue = (value) => {
        modeling.updateProperties(element, { [id]: value });};

    // Functions to get specific values from the business object

    const getValueFormat = () => {
        return element.businessObject.language || ''; };
    const setValueFormat = value => {
        return modeling.updateProperties(element, {
            language: value
        });
    };

    const getValueConditionExpression = () => {
        return element.businessObject.expression || ''; };

    const setValueConditionExpression = value => {
        return modeling.updateProperties(element, {
          expression: value
        });};

    const getValueScriptType = () => {
        return element.businessObject.typeScript || '';};

    const setValueScriptType = value => {
        return modeling.updateProperties(element, {
            typeScript: value
        }); };

    const getValueRessource = () => {
        return element.businessObject.resource || '';};

    const setValueRessource = value => {
        return modeling.updateProperties(element, {
            resource: value
        });};


    const getValueScript = () => {
        return element.businessObject.script || '';};

    const setValueScript = value => {
        return modeling.updateProperties(element, {
            script: value
        });};

    // Function to define options for select fields
    const getOptions = () => {
        return [
            { label: 'Script', value: 'script' },
            { label: 'Expression', value: 'expression' },

        ];
    }

    // Function to define options for script types
    const getOptionsTypes = () => {
        return [
            { label: 'External resource', value: 'externalResource' },
            { label: 'Inline script', value: '' },


        ];
    }
    // Function to render text field based on selected value
    const renderTextFieldTypes = () => {
        const selectedValue = getValueScriptType();

        switch (selectedValue) {
            case 'externalResource':
                return html`
                    <${TextFieldEntry}
                        id=${id}
                        element=${element}
                        label=${translate('Resource')}
                        getValue=${getValueRessource}
                        setValue=${setValueRessource}
                        debounce=${debounce}
                    />
                `;

            case '':
            default:
                return html`
                    <${TextAreaEntry}
                        id=${id}
                        element=${element}
                        label=${translate('Script')}
                        getValue=${getValueScript}
                        setValue=${setValueScript}
                        debounce=${debounce}
                    />
                `;
        }
    };

    // Function to render text field based on selected condition type
    const renderTextField = () => {
        const selectedValue = getValue();

        switch (selectedValue) {
            case 'script':
                return html`
                    <${TextFieldEntry}
                        id=${id}
                        element=${element}
                        label=${translate('Format')}
                        getValue=${getValueFormat}
                        setValue=${setValueFormat}
                        debounce=${debounce}
                    />
                     <div>
            <${SelectEntry}
                id=${id}
                element=${element}
                label=${translate('Script type')}
                getValue=${getValueScriptType}
                setValue=${setValueScriptType}
                getOptions=${getOptionsTypes}
                debounce=${debounce}
            />
             ${renderTextFieldTypes()}
        </div>

                `;

            case 'expression':
                return html`
                    <${TextFieldEntry}
                        id=${id}
                        element=${element}
                        label=${translate('Condition Expression')}
                        getValue=${getValueConditionExpression}
                        setValue=${setValueConditionExpression}
                        debounce=${debounce}
                    />
                `;
            default:
                return null;
        }
    };

    // Return the rendered HTML
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




































































