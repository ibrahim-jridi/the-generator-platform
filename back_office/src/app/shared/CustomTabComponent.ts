import {Components} from 'formiojs';

const TabsComponent = Components.components.tabs;

class CustomTabComponent extends TabsComponent {
  activeTabIndex: number = 0;

  static get builderInfo() {
    return {
      title: 'Custom stepper',
      icon: 'folder-o',
      weight: 50,
      schema: this.schema(),
    };
  }

  static override schema(...extend) {
    return TabsComponent.schema({
      label: 'Custom Tabs',
      type: 'customtabs',
      key: 'customtabs',
      components: [{
        label: 'Tab 1',
        key: 'tab1',
        components: [],
      }],
    });
  }

  constructor(component, options, data) {
    super(component, options, data);
    this.activeTabIndex = 0;
    if (!options || !options.builder) {
      this.injectCustomCSS();
    }
  }

  override attach(element) {
    const attached = super.attach(element);
    element.classList.add('custom-tabs-field');

    this.attachTabEvents(element);
    this.addNavigationButtons(element);

    this.switchTab(this.activeTabIndex);

    return attached;
  }

  attachTabEvents(element) {
    const tabLinks = element.querySelectorAll('.nav-tabs .nav-item');
    tabLinks.forEach((tabLink, index) => {
      const link = tabLink.querySelector('a');
      link.textContent = (index + 1).toString();

      const label = document.createElement('div');
      label.className = 'nav-item-label';
      label.textContent = this.component.components[index].label || `Tab ${index + 1}`;
      tabLink.appendChild(label);

      link.addEventListener('click', (event) => {
        event.preventDefault();
        this.switchTab(index);
      });
    });
  }

  addNavigationButtons(element) {
    let buttonContainer = element.querySelector('.stepper-button-container');
    if (!buttonContainer) {
      buttonContainer = document.createElement('div');
      buttonContainer.className = 'stepper-button-container';

      const prevButton = document.createElement('button');
      prevButton.textContent = 'Précédent';
      prevButton.className = 'stepper-button prev-button';
      prevButton.addEventListener('click', (event) => {
        event.preventDefault();
        this.prevStep();
      });

      const nextButton = document.createElement('button');
      nextButton.textContent = 'Suivant';
      nextButton.className = 'stepper-button next-button';
      nextButton.addEventListener('click', (event) => {
        event.preventDefault();
        this.nextStep();

      });

      buttonContainer.appendChild(prevButton);
      buttonContainer.appendChild(nextButton);
      element.appendChild(buttonContainer);
    }

    this.updateButtonVisibility(buttonContainer);
  }

  updateButtonVisibility(buttonContainer) {
    const prevButton = buttonContainer.querySelector('.prev-button');
    const nextButton = buttonContainer.querySelector('.next-button');

    if (this.activeTabIndex === 0) {
      prevButton.style.display = 'none';
    } else {
      prevButton.style.display = 'inline-block';
    }

    if (this.activeTabIndex === this.component.components.length - 1) {
      nextButton.style.display = 'none';
    } else {
      nextButton.style.display = 'inline-block';
    }
  }

  submitForm() {
    try {
      const customSubmitButton = this.root.components.find(
          (comp) => comp.component.key === 'customSubmit' && comp.component.type === 'button' && comp.component.action === 'custom'
      );

      if (customSubmitButton && customSubmitButton.component.custom) {
        customSubmitButton.component.visible = false;

        const customLogic = customSubmitButton.component.custom;

        const formInstance = this.root;
        new Function('instance', customLogic)(formInstance);
      } else {
        console.warn('No customSubmit button found or no custom logic defined.');
      }
    } catch (error) {
      console.error('Error executing custom submit button logic:', error);
    }
  }

  nextStep() {
    if (this.activeTabIndex < this.component.components.length - 1) {
      this.switchTab(this.activeTabIndex + 1);
    }
  }

  prevStep() {
    if (this.activeTabIndex > 0) {
      this.switchTab(this.activeTabIndex - 1);
    }
  }

  switchTab(index) {
    this.activeTabIndex = index;

    const tabPanes = this.element.querySelectorAll('.tab-pane');
    tabPanes.forEach((pane, i) => {
      if (i === index) {
        pane.style.display = 'block';
      } else {
        pane.style.display = 'none';
      }
    });

    const tabLinks = this.element.querySelectorAll('.nav-link');
    tabLinks.forEach((link, i) => {
      if (i === index) {
        link.classList.add('active');
      } else {
        link.classList.remove('active');
      }
    });

    const buttonContainer = this.element.querySelector('.stepper-button-container');
    if (buttonContainer) {
      this.updateButtonVisibility(buttonContainer);
    }
  }

  injectCustomCSS() {
    const css = `
    .custom-tabs-field .nav-tabs {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0;
      position: relative;
    }

    .custom-tabs-field .nav-tabs::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      right: 0;
      height: 2px;
      background-color: #ccc;
      z-index: 0;
    }

    .custom-tabs-field .nav-item {
      position: relative;
      z-index: 1;
      display: flex;
      flex-direction: column; /* Stack the circle and label */
      align-items: center;
      justify-content: center;
      flex: 1;
    }

    .custom-tabs-field .nav-link {
      border-radius: 50%;
      width: 50px;
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;
      padding: 0;
      margin: 0;
      background-color: #007bff;
      color: white;
      border: 2px solid #007bff;
      transition: background-color 0.3s ease, border-color 0.3s ease;
    }

    .custom-tabs-field .nav-link.active {
      background-color: white;
      color: #007bff;
      border-color: #007bff;
    }

    .custom-tabs-field .nav-link:hover {
      background-color: #0056b3;
      color: white;
      border-color: #0056b3;
    }

    .custom-tabs-field .nav-item-label {
      margin-top: 5px; /* Space between circle and label */
      color: #007bff; /* Label color */
    }

    .custom-tabs-field .nav-item::after {
      content: '';
      position: absolute;
      top: 50%;
      right: 0;
      height: 2px;
      width: 100%;
      background-color: #ccc;
      z-index: -1;
    }

    .custom-tabs-field .nav-item:first-child::before {
      content: none;
    }

    .custom-tabs-field .nav-item:last-child::after {
      content: none;
    }

    .stepper-button-container {
      margin-top: 20px;
      display: flex;
      justify-content: center; /* Center buttons horizontally */
      width: 100%; /* Ensure the container takes full width */
    }

    .stepper-button {
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      width: 150px; /* Fixed width for the buttons */
      height: 40px; /* Equal height for both buttons */
      margin: 0 10px; /* Adds some space between the buttons */
      transition: background-color 0.3s ease, border-color 0.3s ease;
    }

    .stepper-button:hover {
      background-color: #0056b3;
    }

    .stepper-button:disabled {
      background-color: #ccc;
      cursor: not-allowed;
    }
    `;

    const style = document.createElement('style');
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));

    document.head.appendChild(style);
  }
}

export default CustomTabComponent;
Components.addComponent('customtabs', CustomTabComponent);
