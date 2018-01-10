import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {PmHealthService} from '../../services/pmhealth.service';

@Component({
  selector: 'app-active-visits',
  templateUrl: './active-visits.component.html',
  styleUrls: ['./active-visits.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ActiveVisitsComponent implements OnInit {
  visit: any = null;
  visits: any = [];

  constructor(private pmhealth: PmHealthService) { }

  ngOnInit() {
    this.pmhealth.getAllVisits()
      .then(response => {
        for (const visit of response) {
          if (visit.dischargeDate === null) {
            this.visits.push(visit);

            let notes = '';
            for (const note of visit.notes) {
              notes += note;
            }
          }
        }
      });
  }

  setVisit(visit: any) {
    this.visit = visit;
  }

  endVisit() {

  }

  updateVisit() {

  }
}
