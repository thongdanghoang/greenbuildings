import {CommonModule, DatePipe, NgOptimizedImage} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {AccordionModule} from 'primeng/accordion';
import {AutoFocusModule} from 'primeng/autofocus';
import {AvatarModule} from 'primeng/avatar';
import {BadgeModule} from 'primeng/badge';
import {ButtonModule} from 'primeng/button';
import {CardModule} from 'primeng/card';
import {CheckboxModule} from 'primeng/checkbox';
import {DatePickerModule} from 'primeng/datepicker';
import {DialogModule} from 'primeng/dialog';
import {DividerModule} from 'primeng/divider';
import {DrawerModule} from 'primeng/drawer';
import {DialogService, DynamicDialog} from 'primeng/dynamicdialog';
import {FloatLabelModule} from 'primeng/floatlabel';
import {FluidModule} from 'primeng/fluid';
import {IconField} from 'primeng/iconfield';
import {InputIcon} from 'primeng/inputicon';
import {InputNumberModule} from 'primeng/inputnumber';
import {InputSwitchModule} from 'primeng/inputswitch';
import {InputTextModule} from 'primeng/inputtext';
import {MenuModule} from 'primeng/menu';
import {MenubarModule} from 'primeng/menubar';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {MultiSelect} from 'primeng/multiselect';
import {PaginatorModule} from 'primeng/paginator';
import {PasswordModule} from 'primeng/password';
import {RippleModule} from 'primeng/ripple';
import {Select} from 'primeng/select';
import {SelectButtonModule} from 'primeng/selectbutton';
import {TableModule} from 'primeng/table';
import {TabMenuModule} from 'primeng/tabmenu';
import {TagModule} from 'primeng/tag';
import {ToastModule} from 'primeng/toast';
import {ToggleSwitch} from 'primeng/toggleswitch';
import {CardTemplateComponent} from './components/card/card-template/card-template.component';
import {ConfirmDialogComponent} from './components/dialog/confirm-dialog/confirm-dialog.component';
import {FormFieldErrorComponent} from './components/form/form-field-error/form-field-error.component';
import {PaymentStatusComponent} from './components/payment-status/payment-status.component';
import {TableTemplateComponent} from './components/table-template/table-template.component';
import {ErrorMessagesDirective} from './directives/error-messages.directive';
import {FormFieldErrorDirective} from './directives/form-field-error.directive';
import {TranslateParamsPipe} from './pipes/translate-params.pipe';
import {ModalProvider} from './services/modal-provider';
import {SliderModule} from 'primeng/slider';

const primeNgModules = [
  AccordionModule,
  AutoFocusModule,
  AvatarModule,
  BadgeModule,
  ButtonModule,
  CardModule,
  CheckboxModule,
  DatePickerModule,
  DialogModule,
  DrawerModule,
  DynamicDialog,
  DividerModule,
  FloatLabelModule,
  FluidModule,
  InputNumberModule,
  IconField,
  InputIcon,
  InputTextModule,
  InputSwitchModule,
  MessageModule,
  MessagesModule,
  MenuModule,
  MenubarModule,
  Select,
  SelectButtonModule,
  TableModule,
  TabMenuModule,
  ToastModule,
  PaginatorModule,
  PasswordModule,
  RippleModule,
  MultiSelect,
  ToggleSwitch,
  TagModule,
  SliderModule,
  InputNumberModule
];

const commons = [
  CommonModule,
  RouterModule,
  HttpClientModule,
  FormsModule,
  ReactiveFormsModule,
  NgOptimizedImage,
  TranslateModule
];

@NgModule({
  declarations: [
    ConfirmDialogComponent,
    TranslateParamsPipe,
    TableTemplateComponent,
    ErrorMessagesDirective,
    FormFieldErrorDirective,
    FormFieldErrorComponent,
    PaymentStatusComponent,
    CardTemplateComponent
  ],
  imports: [...commons, ...primeNgModules],
  exports: [
    ...commons,
    ...primeNgModules,
    ConfirmDialogComponent,
    TranslateParamsPipe,
    TableTemplateComponent,
    ErrorMessagesDirective,
    FormFieldErrorDirective,
    FormFieldErrorComponent,
    PaymentStatusComponent,
    CardTemplateComponent
  ],
  providers: [DatePipe, ModalProvider, DialogService, DynamicDialog]
})
export class SharedModule {}
