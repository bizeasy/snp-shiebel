import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PostalAddress e2e test', () => {
  const postalAddressPageUrl = '/postal-address';
  let username: string;
  let password: string;
  const postalAddressSample = {};

  let postalAddress;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/postal-addresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/postal-addresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/postal-addresses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (postalAddress) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/postal-addresses/${postalAddress.id}`,
      }).then(() => {
        postalAddress = undefined;
      });
    }
  });

  it('PostalAddresses menu should load PostalAddresses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('postal-address');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostalAddress').should('exist');
    cy.location('pathname').should('eq', postalAddressPageUrl);
  });

  describe('PostalAddress page', () => {
    it('should have translated page title', () => {
      cy.visit(postalAddressPageUrl);
      cy.getEntityHeading('PostalAddress').should('not.contain', 'snpApp.postalAddress.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postalAddressPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostalAddress page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${postalAddressPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PostalAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', postalAddressPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/postal-addresses',
          body: postalAddressSample,
        }).then(({ body }) => {
          postalAddress = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/postal-addresses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/postal-addresses?page=0&size=20>; rel="last",<http://localhost/api/postal-addresses?page=0&size=20>; rel="first"',
              },
              body: [postalAddress],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postalAddressPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostalAddress page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postalAddress');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', postalAddressPageUrl);
      });

      it('edit button click should load edit PostalAddress page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostalAddress');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', postalAddressPageUrl);
      });

      it('edit button click should load edit PostalAddress page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostalAddress');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', postalAddressPageUrl);
      });

      it('last delete button click should delete instance of PostalAddress', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('postalAddress').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', postalAddressPageUrl);

        postalAddress = undefined;
      });
    });
  });

  describe('new PostalAddress page', () => {
    beforeEach(() => {
      cy.visit(postalAddressPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostalAddress');
    });

    it('should create an instance of PostalAddress', () => {
      cy.get(`[data-cy="toName"]`).type('when bad vibration');
      cy.get(`[data-cy="toName"]`).should('have.value', 'when bad vibration');

      cy.get(`[data-cy="address1"]`).type('white');
      cy.get(`[data-cy="address1"]`).should('have.value', 'white');

      cy.get(`[data-cy="address2"]`).type('energetically yuck afterwards');
      cy.get(`[data-cy="address2"]`).should('have.value', 'energetically yuck afterwards');

      cy.get(`[data-cy="city"]`).type('New Mariacester');
      cy.get(`[data-cy="city"]`).should('have.value', 'New Mariacester');

      cy.get(`[data-cy="landmark"]`).type('upbeat bidet');
      cy.get(`[data-cy="landmark"]`).should('have.value', 'upbeat bidet');

      cy.get(`[data-cy="postalCode"]`).type('sore direc');
      cy.get(`[data-cy="postalCode"]`).should('have.value', 'sore direc');

      cy.get(`[data-cy="isDefault"]`).should('not.be.checked');
      cy.get(`[data-cy="isDefault"]`).click();
      cy.get(`[data-cy="isDefault"]`).should('be.checked');

      cy.get(`[data-cy="customAddressType"]`).type('border where unto');
      cy.get(`[data-cy="customAddressType"]`).should('have.value', 'border where unto');

      cy.get(`[data-cy="stateStr"]`).type('after ah');
      cy.get(`[data-cy="stateStr"]`).should('have.value', 'after ah');

      cy.get(`[data-cy="countryStr"]`).type('bah versus oof');
      cy.get(`[data-cy="countryStr"]`).should('have.value', 'bah versus oof');

      cy.get(`[data-cy="isIndianAddress"]`).should('not.be.checked');
      cy.get(`[data-cy="isIndianAddress"]`).click();
      cy.get(`[data-cy="isIndianAddress"]`).should('be.checked');

      cy.get(`[data-cy="note"]`).type('bitterly ugh amid');
      cy.get(`[data-cy="note"]`).should('have.value', 'bitterly ugh amid');

      cy.get(`[data-cy="directions"]`).type('sparse ack shear');
      cy.get(`[data-cy="directions"]`).should('have.value', 'sparse ack shear');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postalAddress = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', postalAddressPageUrl);
    });
  });
});
