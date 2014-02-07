// Copyright 2011 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview CSS3 transition base library.
 *
 */

goog.provide('goog.fx.css3.Transition');

goog.require('goog.Timer');
goog.require('goog.events.EventTarget');
goog.require('goog.fx.Transition');  // Unreferenced: interface
goog.require('goog.fx.Transition.EventType');
goog.require('goog.style');
goog.require('goog.style.transition');



/**
 * A class to handle targeted CSS3 transition. This class
 * handles common features required for targeted CSS3 transition.
 *
 * Browser that does not support CSS3 transition will still receive all
 * the events fired by the transition object, but will not have any transition
 * played. If the browser supports the final state as set in setFinalState
 * method, the element will ends in the final state.
 *
 * Transitioning multiple properties with the same setting is possible
 * by setting Css3Property's property to 'all'. Performing multiple
 * transitions can be done via setting multiple initialStyle,
 * finalStyle and transitions. Css3Property's delay can be used to
 * delay one of the transition. Here is an example for a transition
 * that expands on the width and then followed by the height:
 *
 * <pre>
 *   initialStyle: {width: 10px, height: 10px}
 *   finalStyle: {width: 100px, height: 100px}
 *   transitions: [
 *     {property: width, duration: 1, timing: 'ease-in', delay: 0},
 *     {property: height, duration: 1, timing: 'ease-in', delay: 1}
 *   ]
 * </pre>
 *
 * @param {Element} element The element to be transitioned.
 * @param {number} duration The duration of the transition in seconds.
 *     This should be the longest of all transitions.
 * @param {Object} initialStyle Initial style properties of the element before
 *     animating. Set using {@code goog.style.setStyle}.
 * @param {Object} finalStyle Final style properties of the element after
 *     animating. Set using {@code goog.style.setStyle}.
 * @param {goog.style.transition.Css3Property|
 *     Array.<goog.style.transition.Css3Property>} transitions A single CSS3
 *     transition property or an array of it.
 * @extends {goog.events.EventTarget}
 * @implements {goog.fx.Transition}
 * @constructor
 */
goog.fx.css3.Transition = function(
    element, duration, initialStyle, finalStyle, transitions) {
  goog.base(this);

  /**
   * @type {Element}
   * @private
   */
  this.element_ = element;

  /**
   * @type {number}
   * @private
   */
  this.duration_ = duration;

  /**
   * @type {Object}
   * @private
   */
  this.initialStyle_ = initialStyle;

  /**
   * @type {Object}
   * @private
   */
  this.finalStyle_ = finalStyle;

  /**
   * @type {Array.<goog.style.transition.Css3Property>}
   * @private
   */
  this.transitions_ = goog.isArray(transitions) ? transitions : [transitions];
};
goog.inherits(goog.fx.css3.Transition, goog.events.EventTarget);


/**
 * @type {boolean}
 * @private
 */
goog.fx.css3.Transition.prototype.isPlaying_ = false;


/**
 * Timer id to be used to cancel animation part-way.
 * @type {number}
 * @private
 */
goog.fx.css3.Transition.prototype.timerId_;


/** @inheritDoc */
goog.fx.css3.Transition.prototype.play = function() {
  if (!this.dispatchEvent(goog.fx.Transition.EventType.PLAY) ||
      !this.dispatchEvent(goog.fx.Transition.EventType.BEGIN)) return;

  this.isPlaying_ = true;

  if (goog.style.transition.isSupported()) {
    goog.style.setStyle(this.element_, this.initialStyle_);
    // Allow element to get updated to its initial state before installing
    // CSS3 transition.
    goog.Timer.callOnce(this.play_, undefined, this);
  } else {
    this.stop_();
  }
};


/**
 * Helper method for play method. This needs to be executed on a timer.
 * @private
 */
goog.fx.css3.Transition.prototype.play_ = function() {
  goog.style.transition.set(this.element_, this.transitions_);
  goog.style.setStyle(this.element_, this.finalStyle_);
  this.timerId_ = goog.Timer.callOnce(this.stop_, this.duration_ * 1000, this);
};


/** @inheritDoc */
goog.fx.css3.Transition.prototype.stop = function() {
  if (!this.isPlaying_) return;

  if (this.timerId_) {
    goog.Timer.clear(this.timerId_);
    this.timerId_ = 0;
  }
  this.stop_();
};


/**
 * Helper method for stop method.
 * @private
 */
goog.fx.css3.Transition.prototype.stop_ = function() {
  this.isPlaying_ = false;
  goog.style.transition.removeAll(this.element_);

  // Make sure that we have reached the final style.
  goog.style.setStyle(this.element_, this.finalStyle_);

  this.dispatchEvent(goog.fx.Transition.EventType.END);
  this.dispatchEvent(goog.fx.Transition.EventType.FINISH);
  this.dispatchEvent(goog.fx.Transition.EventType.STOP);
};


/** @inheritDoc */
goog.fx.css3.Transition.prototype.disposeInternal = function() {
  this.stop();
  goog.base(this, 'disposeInternal');
};
