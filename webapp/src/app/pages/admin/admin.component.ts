import {Component, OnInit} from '@angular/core';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {ApiService} from '../../core/api.service';

@Component({
  selector: 'openmeed-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {


  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = {percentage: 0};
  state = 'Upload';
  email;

  constructor(private http: ApiService) {
  }

  ngOnInit() {
  }

  selectEmail(event) {
    this.email = event.target.value;
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }


}
