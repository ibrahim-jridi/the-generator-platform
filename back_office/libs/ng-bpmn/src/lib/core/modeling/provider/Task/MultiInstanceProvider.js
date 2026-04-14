import { is } from 'bpmn-js/lib/util/ModelUtil';
import { CheckboxEntry, TextFieldEntry } from '@bpmn-io/properties-panel';
import { html } from 'htm/preact';



const LOW_PRIORITY = 500;

export default function MultiInstanceProvider(propertiesPanel, translate, eventBus, injector) {

  this.getGroups = function(element) {
    return function(groups) {


      if ((is(element, 'bpmn:Task') || is(element, 'bpmn:CallActivity') ) && element.businessObject.loopCharacteristics) {
        const loopCharacteristics = element.businessObject.loopCharacteristics;
        const multiInstanceGroup = groups.find(group => group.id === 'multiInstance');
        const debounce = injector.get('debounceInput');

        if (multiInstanceGroup) {
          multiInstanceGroup.entries.push({
            id: 'collection',
            component: ({ element, id }) => {
              const getValue = () => loopCharacteristics.get('camunda:collection') || '';
              const setValue = (value) => {
                loopCharacteristics.set('camunda:collection', value);
                return true;
              };

              return html`<${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Collection')}
            getValue=${getValue}
            setValue=${setValue}
            debounce=${debounce}
          />`;
            },
          });

          multiInstanceGroup.entries.push({
            id: 'elementVariable',
            component: ({ element, id }) => {
              const getValue = () => loopCharacteristics.get('camunda:elementVariable') || '';
              const setValue = (value) => {
                loopCharacteristics.set('camunda:elementVariable', value);
                return true;
              };

              return html`<${TextFieldEntry}
            id=${id}
            element=${element}
            label=${translate('Element variable')}
            getValue=${getValue}
            setValue=${setValue}
            debounce=${debounce}
          />`;
            },
          });

          multiInstanceGroup.entries.push({
            id: 'asynchronousBefore',
            component: ({ element, id }) => {
              const getValue = () => loopCharacteristics.get('camunda:asyncBefore') || false;
              const setValue = (value) => {
                loopCharacteristics.set('camunda:asyncBefore', value);
                updateDynamicEntries();
                return true;
              };

              return html`<${CheckboxEntry}
            id=${id}
            element=${element}
            label=${translate('Asynchronous before')}
            getValue=${getValue}
            setValue=${setValue}
            debounce=${debounce}
          />`;
            },
          });

          multiInstanceGroup.entries.push({
            id: 'asynchronousAfter',
            component: ({ element, id }) => {
              const getValue = () => loopCharacteristics.get('camunda:asyncAfter') || false;
              const setValue = (value) => {
                loopCharacteristics.set('camunda:asyncAfter', value);
                updateDynamicEntries();
                return true;
              };
              return html`<${CheckboxEntry}
            id=${id}
            element=${element}
            label=${translate('Asynchronous after')}
            getValue=${getValue}
            setValue=${setValue}
            debounce=${debounce}
          />`;
            },
          });

          const updateDynamicEntries = () => {
             const asyncAfter = loopCharacteristics.get('camunda:asyncAfter') || false;
             const asyncBefore = loopCharacteristics.get('camunda:asyncBefore') || false;

            if (asyncAfter || asyncBefore) {
              const exclusiveExists = multiInstanceGroup.entries?.some(entry => entry.id === 'exclusive');
              const retryTimeCycleExists = multiInstanceGroup.entries?.some(entry => entry.id === 'failedJobRetryTimeCycle');

              if (!exclusiveExists) {
                multiInstanceGroup.entries.push({
                  id: 'exclusive',
                  component: ({ element, id }) => {
                    const getValue = () => loopCharacteristics.get('camunda:exclusive', true);
                    const setValue = (value) => {
                      loopCharacteristics.set('camunda:exclusive', value);
                      return true;
                    };

                    return html`<${CheckboxEntry}
                      id=${id}
                      element=${element}
                      label=${translate('Exclusive')}
                      getValue=${getValue}
                      setValue=${setValue}
                      debounce=${debounce}
                    />`;
                  },
                });
              }

              if (!retryTimeCycleExists) {
                multiInstanceGroup.entries.push({
                  id: 'failedJobRetryTimeCycle',
                  component: ({ element, id }) => {
                    const getValue = () => loopCharacteristics.get('camunda:failedJobRetryTimeCycle', '');
                    const setValue = (value) => {
                      loopCharacteristics.set('camunda:failedJobRetryTimeCycle', value);
                      return true;
                    };

                    return html`<${TextFieldEntry}
                      id=${id}
                      element=${element}
                      label=${translate('Retry time cycle')}
                      getValue=${getValue}
                      setValue=${setValue}
                      debounce=${debounce}
                    />`;
                   },
                 });
              }
             }
           };


           updateDynamicEntries();
          }
        eventBus.fire('propertiesPanel.refresh');

        }
      console.log(groups);

      return groups;
    };
  };

  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

MultiInstanceProvider.$inject = ['propertiesPanel', 'translate', 'eventBus', 'injector'];


