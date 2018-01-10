import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import {PmHealthService} from '../services/pmhealth.service';

@Component({
  selector: 'app-medicines',
  templateUrl: './medicines.component.html',
  styleUrls: ['./medicines.component.css']
})
export class MedicinesComponent implements OnInit {
  medicines = [];

  constructor(private pmhealth: PmHealthService) { }

  ngOnInit() {
    this.pmhealth.getMedications()
      .then(response => this.medicines = response
      );
  }

}
