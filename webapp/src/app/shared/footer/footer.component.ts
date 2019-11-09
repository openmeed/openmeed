import {Component, HostListener, OnInit, TemplateRef} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {ApiService} from '../../core/api.service';

@Component({
  selector: 'openmeed-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  email;
  modalRef: BsModalRef;
  isShow: boolean;
  topPosToStartShowing = 100;

  date = function () {
    return new Date().getUTCFullYear();
  };

  constructor(private apiService: ApiService, private modalService: BsModalService) {
  }

  ngOnInit() {
  }


  @HostListener('window:scroll')
  checkScroll() {
    const scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;

    if (scrollPosition >= this.topPosToStartShowing) {
      this.isShow = true;
    } else {
      this.isShow = false;
    }
  }

  gotoTop() {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
    });
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }
}
