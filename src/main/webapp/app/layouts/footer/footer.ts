import { Component } from '@angular/core';

import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'sys-footer',
  templateUrl: './footer.html',
  imports: [TranslateDirective],
})
export default class Footer {}
