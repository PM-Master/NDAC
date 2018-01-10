import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {PmHealthService} from '../services/pmhealth.service';

@Component({
  selector: 'app-my-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MyPatientsComponent implements OnInit {
  records: any;

  constructor(private pmhealth: PmHealthService) {
    this.records = [];
  }

  ngOnInit() {
    this.pmhealth.getPatients()
      .then(response => {
        for (const x of response) {
          console.log(x.name === null);
          if (x.name !== null) {
            this.records.push(x);
          }
        }
      });
  }

}
