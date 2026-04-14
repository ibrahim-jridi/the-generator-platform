import {Components} from 'formiojs';

const ButtonCustomComponent = Components.components.button;

class CustomButtonComponent extends ButtonCustomComponent {
  activeTabIndex: number = 0;

  constructor(component, options, data) {
    super(component, options, data);
    this.activeTabIndex = 0;

    if (!options || !options.builder) {
      this.injectCustomCSS();
    }
  }

  static get builderInfo() {
    return {
      title: 'Custom Button Click',
      icon: 'folder-o',
      weight: 50,
      schema: CustomButtonComponent.schema()
    };
  }

 public static override schema(...extend) {
    return ButtonCustomComponent.schema({
      label: 'Custom Button',
      action: 'custom',
      showValidations: false,
      tableView: false,
      key: 'custombuttonclick',
      type: 'button',
      input: true,
      custom: 'try {\n' +
          '      const formInstance = instance.root;\n' +
          '      if (formInstance && formInstance.checkValidity(formInstance.data, true, formInstance.data)) {\n' +
          '        instance.emit(\'customButtonClick\', \'modal\', \'Operation successful\', \'Success\');\n' +
          '        formInstance.submit();\n' +
          '      } else {\n' +
          '        formInstance.showErrors();\n' +
          '        formInstance.setPristine(false);\n' +
          '        return;\n' +
          '      }\n' +
          '    } catch (error) {\n' +
          '      console.error(\'Error in custom button logic:\', error);\n' +
          '    }'
    });
  }

  override attach(element) {
    element.classList.add('custom-button-class');
    const attached = super.attach(element);

    return attached;
  }

  switchTab(index) {
    this.redraw();
  }

  injectCustomCSS() {
    const css = `.custom-button-class { background-color: lightblue; }`; // Customize button styling
    const style = document.createElement('style');
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));
    document.head.appendChild(style);
  }
}

export default CustomButtonComponent;
Components.addComponent('custombuttonclick', CustomButtonComponent);

