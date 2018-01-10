import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {PmHealthService} from "../../services/pmhealth.service";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-start-visit',
  templateUrl: './start-visit.component.html',
  styleUrls: ['./start-visit.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class StartVisitComponent implements OnInit {
  records: any;
  visit: any;
  visitModel: any = {};

  constructor(private pmhealth: PmHealthService,
              private alertService: AlertService) { }

  ngOnInit() {
    this.pmhealth.getPatients()
      .then(response => this.records = response);
  }

  startVisit(patientId: number) {
    this.visit = {};
    this.pmhealth.startVisit(patientId)
      .then(response => {
        this.visit = response;
        this.visit.patientId = patientId;
      });
  }

  submitVisit() {
    this.pmhealth.updateVisit(this.visit.patientId, this.visit)
      .then(response => {
        if (response === null) {
          this.alertService.error('An error occurred while updating patient info');
        } else {
          this.visit = null;
          this.alertService.success('Visit submitted');
        }
      });
  }

}
