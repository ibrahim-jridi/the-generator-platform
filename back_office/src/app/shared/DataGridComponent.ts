import {Components} from 'formiojs';

const TabsComponent = Components.components.datagrid;

class  DataGridComponent extends TabsComponent {
  activeTabIndex: number = 0;

  static get builderInfo() {
    return {
      title: 'Custom Grid',
      icon: 'folder-o',
      weight: 50,
      schema: this.schema(),
    };
  }

  static override schema(...extend) {
    return TabsComponent.schema({
      label: 'Data Grid',
      type: 'customdatagrid',
      key: 'customdatagrid',
      components: [
        {
          type: 'customdatagrid',
          key: 'customdatagrid',
          label: 'Data Grid',
        }
      ],
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
    element.classList.add('customdatagrid');

    const attached = super.attach(element);

    const tabLinks = element.querySelectorAll('.nav-tabs .nav-item');
    tabLinks.forEach((tabLink, index) => {
      const link = tabLink.querySelector('a');

      link.textContent = (index + 1).toString();

      const label = document.createElement('div');
      label.className = 'nav-item-label';
      label.textContent = this.component.components[index].label || `Tab ${index + 1}`;

      tabLink.appendChild(label);

      link.addEventListener('click', (event) => {
        this.switchTab(index);
      });
    });

    this.updateDataGridButtons(element);
    return attached;
  }

  updateDataGridButtons(element) {
    // Select all rows of the datagrid
    const rows = element.querySelectorAll('.customdatagrid-row');
    rows.forEach((row) => {
      // Ensure there is only one cell and it is styled as a list item
      const removeButton = document.createElement('button');
      removeButton.type = 'button';
      removeButton.className = 'btn btn-secondary formio-button-remove-row';
      removeButton.setAttribute('aria-label', 'remove');
      removeButton.innerHTML = '<i class="fa fa-times-circle-o"></i>';
      removeButton.addEventListener('click', () => {
        console.log('Remove button clicked');
        // Implement row removal logic here
      });
      row.appendChild(removeButton);
    });
  }

  switchTab(index) {
    this.activeTabIndex = index;
    this.redraw();
  }

  setActiveTab(index) {
    this.activeTabIndex = index;
    this.redraw();
  }

  injectCustomCSS() {
    const css = `

    .customdatagrid-table {
      width: 100%;
      border-collapse: collapse;
      border-spacing: 0;
    }

    .customdatagrid-table th,
    .customdatagrid-table td {
      border: 1px solid transparent;
      padding: 8px;
      text-align: left;
    }

    .customdatagrid-table th {
      background-color: #f4f4f4;
      font-weight: bold;
    }

    .customdatagrid-table tr:nth-child(even) {
      background-color: #f9f9f9;
    }

    .customdatagrid-table tr:hover {
      background-color: #f1f1f1;
    }

    .table-bordered td,
    .table-bordered th {
      border: 1px solid transparent;
    }
    .formio-component-customdatagrid .customdatagrid-table th {
      border: 1px solid transparent ;
      padding: 10px;
    }


    .formio-component-customdatagrid .customdatagrid-table td {
      border: 1px solid transparent ;
      padding: 10px;
    }

    .customdatagrid-footer {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      align-items: center;
      gap: 10px;
    }

.formio-component-customdatagrid {
    border: 1px solid #ced4da;
    border-radius: 0.5rem;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    padding: 1rem;
    background-color: #F1F5F9;
    display: flex;
    flex-direction: column;
    height: 100%;
}

.formio-component-customdatagrid .card-footer {
    margin-top: auto;
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.formio-component-customdatagrid .btn.btn-secondary.formio-button-remove-row {
    position: absolute;
    bottom: 1rem;
    right: 1rem;
    color: #dc3545;
    font-size: 16px;
    border: none;
    background-color: transparent;
    padding: 0;
    display: flex;
    align-items: center;
}

.formio-component-customdatagrid .btn.btn-secondary.formio-button-remove-row::before {
    content: '✖';
    font-size: 18px;
    margin-right: 5px;
    color: #dc3545;

}
.formio-component-customdatagrid .btn-primary.formio-button-add-row {
  background-color: #28a745;
  color: #fff;
  border: 1px solid #28a745;
  font-size: 14px;
  border-radius: 25px;
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: auto;
  min-width: 150px;
  text-align: center;
}


  .formio-component-customdatagrid .table td .btn,
    .formio-component-customdatagrid .table th .btn {
      max-height: initial;
      padding: initial;
      border: initial;
    `;
    const style = document.createElement('style');
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));

    document.head.appendChild(style);
  }
}

export default DataGridComponent;
Components.addComponent('customdatagrid', DataGridComponent);
