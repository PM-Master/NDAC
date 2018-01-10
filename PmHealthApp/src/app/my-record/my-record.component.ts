import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PmHealthService} from '../services/pmhealth.service';
import {AlertService} from '../services/alert.service';

@Component({
  selector: 'app-my-record',
  templateUrl: './my-record.component.html',
  styleUrls: ['./my-record.component.css']
})
export class MyRecordComponent implements OnInit {
  patientId: number;
  currentVisit: any = {};
  oldCurrentVisit: any = {};

  infoModel: any = {};
  oldInfoModel: any = {};
  visitModel: any = {};
  vitalsModel: any = null;
  diagModel = [];

  constructor(private route: ActivatedRoute,
              private pmhealth: PmHealthService,
              private alertService: AlertService,
              private router: Router) {
    this.currentVisit.vitals = {};
    this.oldCurrentVisit.vitals = {};
  }

  ngOnInit() {
    this.pmhealth.getSessionPatientId()
      .then(response => {
        this.patientId = response;
        console.log(this.patientId);
        this.router
          .navigate([`/patient-detail/${this.patientId}`], {queryParams: {session: 'true'}});
      });
  }
}

