import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {PmHealthService} from '../services/pmhealth.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DashboardComponent implements OnInit {
  links = [];
  constructor(private pmhealth: PmHealthService) { }

  ngOnInit() {
    this.pmhealth.getDashboard()
      .then(res => {
        console.log(res);
        this.links = res;
      });
  }

  getBackColor(card: string): string {
    switch (card) {
      case'Patients':
        return '#008cba'; // blue
      case 'Request Access':
        return '#43ac6a'; // green
      case 'Medicines':
        return '#F04124'; // red
      case 'My Record':
        return '#008cba'; // blue
      case  'Messages':
        return '#5bc0de'; // light blue
      case 'Actions':
        return '#BF76C5'; // purple
      case 'Start Visit':
        return '#008cba'; // blue
      case 'Active Visits':
        return '#43ac6a'; // green
    }
  }

  getFooterBackColor(card: string): string {
    switch (card) {
      case'Patients':
        return '#007399'; // blue
      case 'Request Access':
        return '#39935a'; // green
      case 'Medicines':
        return '#d72d0f'; // red
      case 'My Record':
        return '#007399'; // blue
      case  'Messages':
        return '#41b5d8'; // light blue
      case 'Actions':
        return '#A960AF'; // purple
      case 'Start Visit':
        return '#007399'; // blue
      case 'Active Visits':
        return '#39935a'; // green
    }
  }

  toRouterLink(link: string): string {
    return '../' + link.toLowerCase().replace(' ', '-');
  }

}
