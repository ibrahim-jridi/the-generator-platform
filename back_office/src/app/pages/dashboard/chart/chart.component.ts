import { Component, Input } from '@angular/core';
import { ApexOptions } from 'apexcharts';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent {
  @Input() card: any;

  barChartOptions: ApexOptions = {
    chart: {
      type: 'bar',
      background: 'linear-gradient(180deg, #CFE2FD 0%, rgba(13, 110, 253, 0) 100%)',
      width: '100%',
      toolbar: { show: false }
    },
    series: [{ name: 'Active Users', data: [300, 400, 200, 500, 400, 300, 450, 250, 500] }],
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '10%',
        distributed: true,
        borderRadius: 15
      }
    },
    xaxis: {
      labels: { show: false },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: {
      labels: { style: { colors: '#b8c2cc', fontSize: '12px' } },
      min: 0,
      max: 500,
      tickAmount: 5
    },
    grid: {
      yaxis: {
        lines: {
          show: false
        }
      }
    },
    dataLabels: {
      enabled: false
    },
    colors: ['#A6B3C4'],
    legend: {
      show: false
    }
  };

  splineChartOptions: ApexOptions = {
    chart: {
      type: 'area',
      width: '100%',
      toolbar: {
        show: false
      }
    },
    fill: {
      type: 'gradient',
      gradient: {
        shadeIntensity: 1,
        opacityFrom: 0.4,
        opacityTo: 0.1,
        stops: [0, 100],
        colorStops: [
          { offset: 0, color: '#CFE2FD', opacity: 0.4 },
          { offset: 100, color: 'rgba(13, 110, 253, 0)', opacity: 0.1 }
        ]
      }
    },
    stroke: {
      curve: 'smooth',
      width: 3
    },
    series: [
      {
        name: '2024',
        data: [50, 20, 10, 40, 30, 50, 20, 10, 10, 20, 50, 40],
        color: '#B2C6DC'
      },
      {
        name: '2023',
        data: [20, 30, 50, 20, 40, 40, 50, 40, 20, 10, 30, 40],
        color: '#4FD1C5'
      }
    ],
    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      labels: {
        style: {
          colors: '#CBD5E0'
        }
      }
    },
    yaxis: {
      min: 0,
      max: 50,
      tickAmount: 5,
      labels: {
        style: {
          colors: '#CBD5E0'
        }
      }
    },
    dataLabels: {
      enabled: false
    },
    grid: {
      borderColor: '#E2E8F0'
    },
    legend: {
      show: false
    }
  };
}
