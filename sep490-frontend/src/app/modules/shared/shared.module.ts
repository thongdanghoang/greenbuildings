import {
  CommonModule,
  CurrencyPipe,
  DatePipe,
  DecimalPipe,
  NgOptimizedImage
} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import {AccordionModule} from 'primeng/accordion';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {AutoFocusModule} from 'primeng/autofocus';
import {AvatarModule} from 'primeng/avatar';
import {BadgeModule} from 'primeng/badge';
import {BlockUIModule} from 'primeng/blockui';
import {ButtonModule} from 'primeng/button';
import {Calendar} from 'primeng/calendar';
import {CardModule} from 'primeng/card';
import {CarouselModule} from 'primeng/carousel';
import {CheckboxModule} from 'primeng/checkbox';
import {ChipModule} from 'primeng/chip';
import {DatePickerModule} from 'primeng/datepicker';
import {DialogModule} from 'primeng/dialog';
import {DividerModule} from 'primeng/divider';
import {DrawerModule} from 'primeng/drawer';
import {DropdownModule} from 'primeng/dropdown';
import {DialogService, DynamicDialog} from 'primeng/dynamicdialog';
import {Fieldset} from 'primeng/fieldset';
import {FileUpload} from 'primeng/fileupload';
import {FloatLabelModule} from 'primeng/floatlabel';
import {FluidModule} from 'primeng/fluid';
import {IconFieldModule} from 'primeng/iconfield';
import {IftaLabel} from 'primeng/iftalabel';
import {ImageModule} from 'primeng/image';
import {InputGroupModule} from 'primeng/inputgroup';
import {InputGroupAddonModule} from 'primeng/inputgroupaddon';
import {InputIconModule} from 'primeng/inputicon';
import {InputMaskModule} from 'primeng/inputmask';
import {InputNumberModule} from 'primeng/inputnumber';
import {InputOtpModule} from 'primeng/inputotp';
import {InputSwitchModule} from 'primeng/inputswitch';
import {InputTextModule} from 'primeng/inputtext';
import {MenuModule} from 'primeng/menu';
import {MenubarModule} from 'primeng/menubar';
import {MessageModule} from 'primeng/message';
import {MessagesModule} from 'primeng/messages';
import {MultiSelectModule} from 'primeng/multiselect';
import {PaginatorModule} from 'primeng/paginator';
import {PanelModule} from 'primeng/panel';
import {PasswordModule} from 'primeng/password';
import {PopoverModule} from 'primeng/popover';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {RippleModule} from 'primeng/ripple';
import {SelectModule} from 'primeng/select';
import {SelectButtonModule} from 'primeng/selectbutton';
import {SliderModule} from 'primeng/slider';
import {StepperModule} from 'primeng/stepper';
import {TableModule} from 'primeng/table';
import {TabMenuModule} from 'primeng/tabmenu';
import {TagModule} from 'primeng/tag';
import {TextareaModule} from 'primeng/textarea';
import {ToastModule} from 'primeng/toast';
import {ToggleButtonModule} from 'primeng/togglebutton';
import {ToggleSwitchModule} from 'primeng/toggleswitch';
import {TooltipModule} from 'primeng/tooltip';
import {ConfirmDialogComponent} from './components/dialog/confirm-dialog/confirm-dialog.component';
import {FormFieldErrorComponent} from './components/form/form-field-error/form-field-error.component';
import {PaymentStatusComponent} from './components/payment-status/payment-status.component';
import {TableTemplateComponent} from './components/table-template/table-template.component';
import {ErrorMessagesDirective} from './directives/error-messages.directive';
import {FormFieldErrorDirective} from './directives/form-field-error.directive';
import {TranslateParamsPipe} from './pipes/translate-params.pipe';
import {EmissionFactorService} from './services/emission-factor.service';
import {EmissionSourceService} from './services/emission-source.service';
import {EnergyConversionService} from './services/energy-conversion.service';
import {FuelService} from './services/fuel.service';
import {ModalProvider} from './services/modal-provider';
import {ToastProvider} from './services/toast-provider';
import {UnitService} from './services/unit.service';
import {RadioButtonModule} from 'primeng/radiobutton';

const primeNgModules = [
  AccordionModule,
  AutoFocusModule,
  AvatarModule,
  BadgeModule,
  ButtonModule,
  BlockUIModule,
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
  IconFieldModule,
  InputIconModule,
  InputTextModule,
  InputSwitchModule,
  MessageModule,
  MessagesModule,
  MenuModule,
  MenubarModule,
  SelectModule,
  SelectButtonModule,
  TableModule,
  TabMenuModule,
  TextareaModule,
  ToastModule,
  PaginatorModule,
  PasswordModule,
  ProgressSpinnerModule,
  RippleModule,
  MultiSelectModule,
  ToggleSwitchModule,
  TagModule,
  SliderModule,
  InputNumberModule,
  CarouselModule,
  ImageModule,
  InputMaskModule,
  InputOtpModule,
  ToggleButtonModule,
  ChipModule,
  Fieldset,
  IftaLabel,
  StepperModule,
  Calendar,
  DropdownModule,
  FileUpload,
  PopoverModule,
  TooltipModule,
  InputGroupModule,
  InputGroupAddonModule,
  AutoCompleteModule,
  PanelModule,
  RadioButtonModule
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
    PaymentStatusComponent
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
    PaymentStatusComponent
  ],
  providers: [
    DatePipe,
    DecimalPipe,
    CurrencyPipe,
    ModalProvider,
    ToastProvider,
    DialogService,
    DynamicDialog,
    EmissionFactorService,
    EmissionSourceService,
    FuelService,
    EnergyConversionService,
    UnitService
  ]
})
export class SharedModule {}
