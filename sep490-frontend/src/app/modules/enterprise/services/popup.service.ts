import {
  ApplicationRef,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  Injectable,
  Injector,
  Type
} from '@angular/core';
import {SafeHtml} from '@angular/platform-browser';

@Injectable()
export class PopupService {
  constructor(
    private readonly resolver: ComponentFactoryResolver,
    private readonly appRef: ApplicationRef,
    private readonly injector: Injector
  ) {}

  /**
   * Builds the referenced component so it can be injected into the
   * leaflet map as popup.
   * @param component The component to be compiled
   * @param onAttach A function that allows you to assign the component
   * @returns The compiled component
   */
  public compilePopup<T>(component: Type<T>, onAttach: (componentRef: ComponentRef<T>) => void): HTMLDivElement {
    const compFactory: ComponentFactory<T> = this.resolver.resolveComponentFactory(component);
    const compRef: ComponentRef<T> = compFactory.create(this.injector);

    if (onAttach) onAttach(compRef);

    this.appRef.attachView(compRef.hostView);
    compRef.onDestroy((): void => this.appRef.detachView(compRef.hostView));

    const div: HTMLDivElement = document.createElement('div');
    div.appendChild(compRef.location.nativeElement);
    return div;
  }

  makeCapitalPopup(data: any): SafeHtml {
    if (!data?.name || !data?.state || !data?.population) {
      throw new Error('Invalid capital data provided');
    }
    return `
      <div>Capital: ${data.name}</div>
      <div>State: ${data.state}</div>
      <div>Population: ${data.population.toLocaleString()}</div>
    `;
  }
}
